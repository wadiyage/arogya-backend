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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientAppointmentServiceImpl implements PatientAppointmentService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    private final AppointmentService appointmentService;

    private final AppointmentMapper appointmentMapper;

    private final AppointmentAuditService appointmentAuditService;

    @Override
    public AppointmentResponse bookAppointment(UUID userId, CreateAppointmentRequest request) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for userId: " + userId));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        Appointment appointment = appointmentService.book(patient, doctor, request);
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

        if(appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot cancel past appointments.");
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
