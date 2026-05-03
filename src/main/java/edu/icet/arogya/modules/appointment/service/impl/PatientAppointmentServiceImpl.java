package edu.icet.arogya.modules.appointment.service.impl;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogRequest;
import edu.icet.arogya.modules.appointment.audit.entity.enums.AuditActionType;
import edu.icet.arogya.modules.appointment.audit.service.AppointmentAuditService;
import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.dto.CreateAppointmentRequest;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.mapper.AppointmentMapper;
import edu.icet.arogya.modules.appointment.repository.AppointmentRepository;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.appointment.schedule.repository.DoctorScheduleRepository;
import edu.icet.arogya.modules.appointment.service.AppointmentService;
import edu.icet.arogya.modules.appointment.service.PatientAppointmentService;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.doctor.repository.DoctorRepository;
import edu.icet.arogya.modules.patient.entity.Patient;
import edu.icet.arogya.modules.patient.repository.PatientRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientAppointmentServiceImpl implements PatientAppointmentService {

    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AppointmentRepository appointmentRepository;

    private final AppointmentService appointmentService;

    private final AppointmentMapper appointmentMapper;

    private final AppointmentAuditService appointmentAuditService;

    @Override
    public AppointmentResponse bookAppointment(UUID userId, CreateAppointmentRequest request) {
        if(request.getScheduleId() == null) {
            throw new BadRequestException("Schedule ID is required.");
        }

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for userId: " + userId));

        DoctorSchedule schedule = doctorScheduleRepository.findByIdForUpdate(request.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + request.getScheduleId()));

        if(!Boolean.TRUE.equals(schedule.getActive())) {
            throw new BadRequestException("Selected time slot is no longer available.");
        }

        if(schedule.getScheduleDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot book appointments for past dates.");
        }

        if(schedule.getBookedTokens() >= schedule.getMaxTokens()) {
            throw new BadRequestException("Selected time slot is fully booked.");
        }

        boolean alreadyBooked = appointmentRepository.existsByPatientAndScheduleAndDeletedFalse(patient, schedule);

        if(alreadyBooked) {
            throw new BadRequestException("You have already booked an appointment for this time slot.");
        }

        Appointment appointment = appointmentService.book(patient, schedule, request);
        appointmentAuditService.logStatusChange(
                AppointmentAuditLogRequest.builder()
                        .appointment(appointment)
                        .oldStatus(null)
                        .newStatus(appointment.getStatus())
                        .actionType(AuditActionType.APPOINTMENT_BOOKED)
                        .reason("Appointment booked")
                        .userId(userId)
                        .userRole(patient.getUser().getRole().getName())
                        .metadata(null)
                        .build()
        );

        return appointmentMapper.mapToResponse(appointment);
    }

    @Override
    public Page<@NonNull AppointmentResponse> getMyAppointments(UUID userId, Pageable pageable) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for userId: " + userId));

        Page<@NonNull Appointment> page = appointmentRepository.findByPatient(patient, pageable);

        return page.map(appointmentMapper::mapToResponse);
    }

    @Override
    public void cancelAppointment(UUID userId, UUID appointmentId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for userId: " + userId));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        if(!appointment.getPatient().getId().equals(patient.getId())) {
            throw new BadRequestException("Unauthorized access to appointment.");
        }

        AppointmentStatus oldStatus = appointment.getStatus();

        if(oldStatus == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Appointment is already canceled.");
        }

        if(oldStatus == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment.");
        }

        if(oldStatus == AppointmentStatus.IN_PROGRESS) {
            throw new BadRequestException("Cannot cancel an appointment that is in progress.");
        }

        if(appointment.getSchedule().getScheduleDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot cancel past appointments.");
        }

        LocalDateTime cancellationDeadline = appointment.getSchedule().getScheduleDate().atStartOfDay().minusHours(2);
        if(LocalDateTime.now().isAfter(cancellationDeadline)) {
            throw new BadRequestException("Cancellations must be made at least 2 hours before the scheduled time.");
        }

        appointmentService.cancel(appointment);

        appointmentAuditService.logStatusChange(
                AppointmentAuditLogRequest.builder()
                        .appointment(appointment)
                        .oldStatus(oldStatus)
                        .newStatus(AppointmentStatus.CANCELLED)
                        .actionType(AuditActionType.APPOINTMENT_CANCELLED_BY_PATIENT)
                        .reason("Cancelled by patient")
                        .userId(userId)
                        .userRole(patient.getUser().getRole().getName())
                        .metadata(null)
                        .build()
        );
    }
}
