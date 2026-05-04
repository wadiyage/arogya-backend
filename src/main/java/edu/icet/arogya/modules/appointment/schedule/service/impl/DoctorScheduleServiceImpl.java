package edu.icet.arogya.modules.appointment.schedule.service.impl;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.appointment.schedule.repository.DoctorScheduleRepository;
import edu.icet.arogya.modules.appointment.schedule.service.DoctorScheduleService;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.doctor.repository.DoctorRepository;
import edu.icet.arogya.modules.location.entity.Location;
import edu.icet.arogya.modules.location.repository.LocationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorRepository doctorRepository;
    private final LocationRepository locationRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Override
    public DoctorSchedule create(
            UUID doctorId,
            UUID locationId,
            LocalDate date,
            Integer maxTokens,
            String sessionName,
            Double consultationFee
    ) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + locationId));

        if(date.isBefore(LocalDate.now())) {
            throw new BadRequestException("Schedule date cannot be in the past.");
        }

        boolean exists = doctorScheduleRepository.existsByDoctorAndLocationAndScheduleDate(doctor, location, date);

        if (exists) {
            throw new BadRequestException("A schedule already exists for the given doctor, location, and date.");
        }

        DoctorSchedule newSchedule = DoctorSchedule.builder()
                .doctor(doctor)
                .location(location)
                .scheduleDate(date)
                .maxTokens(maxTokens)
                .sessionName(sessionName)
                .consultationFee(consultationFee)
                .build();

        return doctorScheduleRepository.save(newSchedule);
    }

    @Override
    public DoctorSchedule update(
            UUID scheduleId,
            LocalDate date,
            Integer maxTokens,
            String sessionName,
            Double consultationFee
    ) {
        DoctorSchedule schedule = doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + scheduleId));

        if (date != null && !date.equals(schedule.getScheduleDate())) {
            if(schedule.getBookedTokens() > 0) {
                throw new BadRequestException("Cannot change schedule date. There are already booked appointments.");
            }
            schedule.setScheduleDate(date);
        }
        if (maxTokens != null) {
            if(maxTokens < schedule.getBookedTokens()) {
                throw new BadRequestException("Cannot set max tokens less than currently booked tokens.");
            }
            schedule.setMaxTokens(maxTokens);
        }
        if (sessionName != null) {
            schedule.setSessionName(sessionName);
        }
        if (consultationFee != null) {
            schedule.setConsultationFee(consultationFee);
        }

        return doctorScheduleRepository.save(schedule);
    }

    @Override
    public DoctorSchedule getById(UUID scheduleId) {
        return doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule not found with ID: " + scheduleId));
    }

    @Override
    public Page<@NonNull DoctorSchedule> getByDoctor(UUID doctorId, LocalDate date, Pageable pageable) {
        Page<@NonNull DoctorSchedule> schedules = doctorScheduleRepository.findByDoctorIdAndScheduleDate(doctorId, date, pageable);
        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("No schedules found for doctor ID: " + doctorId + " on date: " + date);
        }
        return schedules;
    }

    @Override
    public Page<@NonNull DoctorSchedule> getByLocation(UUID locationId, LocalDate date, Pageable pageable) {
        Page<@NonNull DoctorSchedule> schedules = doctorScheduleRepository.findByLocationIdAndScheduleDate(locationId, date, pageable);
        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("No schedules found for location ID: " + locationId + " on date: " + date);
        }
        return schedules;
    }

    @Override
    public void activate(UUID scheduleId) {
        DoctorSchedule schedule = getById(scheduleId);
        if(Boolean.TRUE.equals(schedule.getActive())) {
            return;
        }
        schedule.setActive(true);
        doctorScheduleRepository.save(schedule);
    }

    @Override
    public void deactivate(UUID scheduleId) {
        DoctorSchedule schedule = getById(scheduleId);
        if(!Boolean.TRUE.equals(schedule.getActive())) {
            return;
        }
        schedule.setActive(false);
        doctorScheduleRepository.save(schedule);
    }
}
