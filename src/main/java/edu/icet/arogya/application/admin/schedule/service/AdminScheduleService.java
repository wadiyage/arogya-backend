package edu.icet.arogya.application.admin.schedule.service;

import edu.icet.arogya.application.admin.schedule.dto.CreateScheduleRequest;
import edu.icet.arogya.application.admin.schedule.dto.ScheduleResponse;
import edu.icet.arogya.application.admin.schedule.dto.UpdateScheduleRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface AdminScheduleService {
    ScheduleResponse create(CreateScheduleRequest request);
    ScheduleResponse update(UUID scheduleId, UpdateScheduleRequest request);

    ScheduleResponse getById(UUID scheduleId);
    Page<@NonNull ScheduleResponse> getByDoctor(UUID doctorId, LocalDate date, Pageable pageable);
    Page<@NonNull ScheduleResponse> getByLocation(UUID locationId, LocalDate date, Pageable pageable);

    void activate(UUID scheduleId);
    void deactivate(UUID scheduleId);
}
