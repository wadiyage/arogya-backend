package edu.icet.arogya.modules.appointment.service.impl;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.common.exception.UnauthorizedException;
import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogRequest;
import edu.icet.arogya.modules.appointment.audit.entity.enums.AuditActionType;
import edu.icet.arogya.modules.appointment.audit.service.AppointmentAuditService;
import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.mapper.AppointmentMapper;
import edu.icet.arogya.modules.appointment.repository.AppointmentRepository;
import edu.icet.arogya.modules.appointment.service.AppointmentService;
import edu.icet.arogya.modules.appointment.service.DoctorAppointmentService;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorAppointmentServiceImpl implements DoctorAppointmentService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    private final AppointmentService appointmentService;

    private final AppointmentAuditService appointmentAuditService;

    @Override
    public List<AppointmentResponse> getMySchedule(UUID userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for userId: " + userId));

        List<Appointment> appointments = appointmentRepository.findTodayQueue(
                doctor,
                LocalDate.now(),
                List.of(
                        AppointmentStatus.CONFIRMED,
                        AppointmentStatus.CHECKED_IN,
                        AppointmentStatus.IN_PROGRESS
                )
        );

        return appointments.stream()
                .sorted(Comparator.comparing(Appointment::getTokenNumber))
                .map(appointmentMapper::mapToResponse)
                .toList();
    }

    @Override
    public AppointmentResponse callNextPatient(UUID userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for userId: " + userId));

        Appointment next = appointmentRepository.findQueueOrdered(doctor, LocalDate.now())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No patients in queue for today"));

        Appointment updated = appointmentService.updateStatus(next, AppointmentStatus.IN_PROGRESS);

        appointmentAuditService.logStatusChange(
                AppointmentAuditLogRequest.builder()
                        .appointment(next)
                        .currentStatus(AppointmentStatus.CHECKED_IN)
                        .newStatus(AppointmentStatus.IN_PROGRESS)
                        .actionType(AuditActionType.APPOINTMENT_STATUS_UPDATED)
                        .reason("Called next patient")
                        .userId(userId)
                        .userRole(doctor.getUser().getRole().getName())
                        .metadata(null)
                        .build()
        );

        return appointmentMapper.mapToResponse(updated);
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(UUID userId, UUID appointmentId, AppointmentStatus newStatus) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for userId: " + userId));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        if(!appointment.getSchedule().getDoctor().getId().equals(doctor.getId())) {
            throw new UnauthorizedException("Appointment does not belong to the specified doctor");
        }

        if(!doctor.isAvailable()) {
            throw new BadRequestException("Doctor is currently unavailable to update appointment status");
        }

        AppointmentStatus currentstatus = appointment.getStatus();

        if(newStatus == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Doctors cannot cancel appointments. Please ask the patient to cancel.");
        }

        if(currentstatus == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot update status of a cancelled appointment");
        }

        if(currentstatus == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot update status of a completed appointment");
        }

        switch (newStatus) {
            case CHECKED_IN -> {
                if (currentstatus != AppointmentStatus.CONFIRMED) {
                    throw new BadRequestException("Only confirmed appointments can be checked in");
                }
            }
            case COMPLETED -> {
                if(currentstatus != AppointmentStatus.IN_PROGRESS) {
                    throw new BadRequestException("Only appointments in progress can be marked as completed");
                }
            }
            default -> throw new BadRequestException("Invalid status update");
        }

        Appointment updated = appointmentService.updateStatus(
                appointment,
                newStatus
        );

        appointmentAuditService.logStatusChange(
                AppointmentAuditLogRequest.builder()
                        .appointment(appointment)
                        .currentStatus(currentstatus)
                        .newStatus(newStatus)
                        .actionType(AuditActionType.APPOINTMENT_STATUS_UPDATED)
                        .reason("Updated by doctor")
                        .userId(userId)
                        .userRole(doctor.getUser().getRole().getName())
                        .metadata(null)
                        .build()
        );

        return appointmentMapper.mapToResponse(updated);
    }
}
