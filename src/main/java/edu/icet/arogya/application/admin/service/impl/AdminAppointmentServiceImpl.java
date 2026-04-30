package edu.icet.arogya.application.admin.service.impl;

import edu.icet.arogya.application.admin.service.AdminAppointmentService;
import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogRequest;
import edu.icet.arogya.modules.appointment.audit.entity.enums.AuditActionType;
import edu.icet.arogya.modules.appointment.audit.service.AppointmentAuditService;
import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.dto.filter.AppointmentFilterRequest;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.mapper.AppointmentMapper;
import edu.icet.arogya.modules.appointment.repository.AppointmentRepository;
import edu.icet.arogya.modules.appointment.service.AppointmentService;
import edu.icet.arogya.modules.appointment.specification.AppointmentSpecification;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static edu.icet.arogya.modules.appointment.validation.AppointmentStatusTransitionValidator.validate;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAppointmentServiceImpl implements AdminAppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    private final AppointmentService appointmentService;
    private final AppointmentAuditService appointmentAuditService;

    @Override
    public Page<@NonNull AppointmentResponse> getAllAppointments(
            AppointmentFilterRequest filter,
            Pageable pageable
    ) {
        Page<@NonNull Appointment> page = appointmentRepository.findAll(
                AppointmentSpecification.withFilters(filter),
                pageable
        );
        return page.map(appointmentMapper::mapToResponse);
    }

    @Caching(evict = {
            @CacheEvict(value = "dashboardOverview", allEntries = true),
            @CacheEvict(value = "dashboardTrends", allEntries = true),
            @CacheEvict(value = "dashboardDoctorWorkload", allEntries = true),
            @CacheEvict(value = "dashboardCancellations", allEntries = true),
            @CacheEvict(value = "dashboardNoShows", allEntries = true)
    })
    @Override
    public AppointmentResponse overrideStatus(
            UUID appointmentId,
            AppointmentStatus status,
            String reason,
            UUID adminId,
            RoleName role
    ) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        AppointmentStatus oldStatus = appointment.getStatus();

        validate(oldStatus, status);

        if(appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot change status of past appointments");
        }

        Appointment updated = appointmentService.updateStatus(appointment, status);

        appointmentAuditService.logStatusChange(
                AppointmentAuditLogRequest.builder()
                        .appointment(updated)
                        .oldStatus(oldStatus)
                        .newStatus(status)
                        .actionType(AuditActionType.APPOINTMENT_STATUS_OVERRIDDEN)
                        .reason(reason)
                        .userId(adminId)
                        .userRole(role)
                        .metadata(null)
                        .build()
        );

        return appointmentMapper.mapToResponse(updated);
    }

    @CacheEvict(value = {
            "dashboard:overview",
            "dashboard:trends",
            "dashboard:doctorWorkload",
            "dashboard:cancellations",
            "dashboard:noShows"
    }, allEntries = true)
    @Override
    public void cancelAppointmentByAdmin(UUID appointmentId, UUID adminId, RoleName role) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        AppointmentStatus oldStatus = appointment.getStatus();
        if(oldStatus == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        if(oldStatus == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Appointment is already cancelled");
        }

        if(appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot change status of past appointments");
        }

        appointmentService.cancel(appointment);

        appointmentAuditService.logStatusChange(
                AppointmentAuditLogRequest.builder()
                        .appointment(appointment)
                        .oldStatus(oldStatus)
                        .newStatus(AppointmentStatus.CANCELLED)
                        .actionType(AuditActionType.APPOINTMENT_CANCELLED_BY_ADMIN)
                        .reason("Force cancelled by admin")
                        .userId(adminId)
                        .userRole(role)
                        .metadata(null)
                        .build()
        );
    }

    @CacheEvict(value = {
            "dashboard:overview",
            "dashboard:trends",
            "dashboard:doctorWorkload",
            "dashboard:cancellations",
            "dashboard:noShows"
    }, allEntries = true)
    @Override
    public void bulkCancelAppointmentsByAdmin(List<UUID> appointmentIds, String reason, UUID adminId, RoleName role) {
        if(appointmentIds == null || appointmentIds.isEmpty()) {
            throw new BadRequestException("Appointment IDs cannot be null or empty");
        }

        for (UUID appointmentId: appointmentIds) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

            AppointmentStatus oldStatus = appointment.getStatus();
            if(oldStatus == AppointmentStatus.COMPLETED || oldStatus == AppointmentStatus.CANCELLED) {
                continue; // Skip appointments that are already completed or cancelled
            }

            appointmentService.cancel(appointment);
            appointmentAuditService.logStatusChange(
                    AppointmentAuditLogRequest.builder()
                            .appointment(appointment)
                            .oldStatus(oldStatus)
                            .newStatus(AppointmentStatus.CANCELLED)
                            .actionType(AuditActionType.APPOINTMENT_BULK_CANCELLED)
                            .reason(reason != null ? reason : "Force cancelled by admin")
                            .userId(adminId)
                            .userRole(role)
                            .metadata(null)
                            .build()
            );
        }
    }
}
