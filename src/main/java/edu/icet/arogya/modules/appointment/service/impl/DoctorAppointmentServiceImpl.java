package edu.icet.arogya.modules.appointment.service.impl;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.common.exception.UnauthorizedException;
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

    @Override
    public List<AppointmentResponse> getMySchedule(UUID userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for userId: " + userId));

        List<Appointment> appointments = appointmentRepository.findByDoctorAndAppointmentDateAndStatusIn(
                doctor,
                LocalDate.now(),
                List.of(
                        AppointmentStatus.CONFIRMED,
                        AppointmentStatus.CHECKED_IN,
                        AppointmentStatus.IN_PROGRESS
                )
        );

        return appointments.stream()
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .map(appointmentMapper::mapToResponse)
                .toList();
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(UUID userId, UUID appointmentId, AppointmentStatus status) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for userId: " + userId));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        if(!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new UnauthorizedException("Appointment does not belong to the specified doctor");
        }

        if(!doctor.isAvailable()) {
            throw new BadRequestException("Doctor is currently unavailable to update appointment status");
        }

        if(status == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Doctors cannot cancel appointments. Please ask the patient to cancel.");
        }

        if(status == AppointmentStatus.COMPLETED && appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
            throw new BadRequestException("Only appointments that are in progress can be marked as completed.");
        }

        Appointment updated = appointmentService.updateStatus(
                appointment,
                status
        );

        return appointmentMapper.mapToResponse(updated);
    }
}
