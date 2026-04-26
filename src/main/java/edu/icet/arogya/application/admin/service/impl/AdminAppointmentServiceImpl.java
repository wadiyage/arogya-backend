package edu.icet.arogya.application.admin.service.impl;

import edu.icet.arogya.application.admin.service.AdminAppointmentService;
import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.appointment.audit.service.AppointmentAuditService;
import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.dto.filter.AppointmentFilterRequest;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.mapper.AppointmentMapper;
import edu.icet.arogya.modules.appointment.repository.AppointmentRepository;
import edu.icet.arogya.modules.appointment.service.AppointmentService;
import edu.icet.arogya.modules.appointment.specification.AppointmentSpecification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Override
    public AppointmentResponse overrideStatus(
            UUID appointmentId,
            AppointmentStatus status,
            String reason
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
                updated,
                oldStatus,
                status,
                reason,
                "ADMIN"

        );

        return appointmentMapper.mapToResponse(updated);
    }

    @Override
    public void cancelAppointmentByAdmin(UUID appointmentId) {
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
                appointment,
                oldStatus,
                AppointmentStatus.CANCELLED,
                "Force cancelled by admin",
                "ADMIN"
        );
    }
}
