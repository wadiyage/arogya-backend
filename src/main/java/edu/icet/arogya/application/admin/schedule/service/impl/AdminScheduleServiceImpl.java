package edu.icet.arogya.application.admin.schedule.service.impl;

import edu.icet.arogya.application.admin.schedule.dto.CreateScheduleRequest;
import edu.icet.arogya.application.admin.schedule.dto.ScheduleResponse;
import edu.icet.arogya.application.admin.schedule.dto.UpdateScheduleRequest;
import edu.icet.arogya.application.admin.schedule.service.AdminScheduleService;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.appointment.schedule.mapper.ScheduleMapper;
import edu.icet.arogya.modules.appointment.schedule.service.DoctorScheduleService;
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
public class AdminScheduleServiceImpl implements AdminScheduleService {

    private final DoctorScheduleService doctorScheduleService;

    private final ScheduleMapper scheduleMapper;

    @Override
    public ScheduleResponse create(CreateScheduleRequest request) {
        DoctorSchedule newSchedule = doctorScheduleService.create(
                request.getDoctorId(),
                request.getLocationId(),
                request.getDate(),
                request.getMaxTokens(),
                request.getSessionName(),
                request.getConsultationFee()
        );

        return scheduleMapper.mapToResponse(newSchedule);
    }

    @Override
    public ScheduleResponse update(UUID scheduleId, UpdateScheduleRequest request) {
        DoctorSchedule exiting = doctorScheduleService.update(
                scheduleId,
                request.getDate(),
                request.getMaxTokens(),
                request.getSessionName(),
                request.getConsultationFee()
        );

        return scheduleMapper.mapToResponse(exiting);
    }

    @Override
    public ScheduleResponse getById(UUID scheduleId) {
        DoctorSchedule schedule = doctorScheduleService.getById(scheduleId);
        return scheduleMapper.mapToResponse(schedule);
    }

    @Override
    public Page<@NonNull ScheduleResponse> getByDoctor(UUID doctorId, LocalDate date, Pageable pageable) {
        return doctorScheduleService.getByDoctor(doctorId, date, pageable)
                .map(scheduleMapper::mapToResponse);
    }

    @Override
    public Page<@NonNull ScheduleResponse> getByLocation(UUID locationId, LocalDate date, Pageable pageable) {
        return doctorScheduleService.getByLocation(locationId, date, pageable)
                .map(scheduleMapper::mapToResponse);
    }

    @Override
    public void activate(UUID scheduleId) {
        doctorScheduleService.activate(scheduleId);
    }

    @Override
    public void deactivate(UUID scheduleId) {
        doctorScheduleService.deactivate(scheduleId);
    }
}
