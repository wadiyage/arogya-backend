package edu.icet.arogya.modules.appointment.service.impl;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.modules.appointment.dto.CreateAppointmentRequest;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.repository.AppointmentRepository;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.appointment.service.AppointmentService;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.patient.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public Appointment book(Patient patient, DoctorSchedule schedule, CreateAppointmentRequest request) {
        int nextToken = schedule.getBookedTokens() + 1;

        if (nextToken > schedule.getMaxTokens()) {
            throw new BadRequestException("No available slots for the selected schedule");
        }

        schedule.setBookedTokens(nextToken);

        if(nextToken == schedule.getMaxTokens()) {
            schedule.setActive(false);
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .schedule(schedule)
                .tokenNumber(nextToken)
                .status(AppointmentStatus.CONFIRMED)
                .reason(request.getReason())
                .notes(request.getNotes())
                .build();

        return appointmentRepository.save(appointment);
    }

    @Override
    public void cancel(Appointment appointment) {
        if(appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Appointment is already canceled");
        }

        DoctorSchedule schedule = appointment.getSchedule();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelledAt(LocalDateTime.now());

        if(schedule.getBookedTokens() > 0) {
            schedule.setBookedTokens(schedule.getBookedTokens() - 1);
        }

        if(!Boolean.TRUE.equals(schedule.getActive())) {
            schedule.setActive(true);
        }

        appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateStatus(Appointment appointment, AppointmentStatus status) {
        if(appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot update status of a cancelled appointment");
        }

        if(appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot update status of a completed appointment");
        }

        appointment.setStatus(status);
        status.apply(appointment);
        return appointmentRepository.save(appointment);
    }
}
