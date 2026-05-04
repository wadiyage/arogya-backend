package edu.icet.arogya.modules.appointment.schedule.service;

import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface DoctorScheduleService {
    DoctorSchedule create(
            UUID doctorId,
            UUID locationId,
            LocalDate date,
            Integer maxTokens,
            String sessionName,
            Double consultationFee
    );
    DoctorSchedule update(
            UUID scheduleId,
            LocalDate date,
            Integer maxTokens,
            String sessionName,
            Double consultationFee
    );

    DoctorSchedule getById(UUID scheduleId);
    Page<@NonNull DoctorSchedule> getByDoctor(UUID doctorId, LocalDate date, Pageable pageable);
    Page<@NonNull DoctorSchedule> getByLocation(UUID locationId, LocalDate date, Pageable pageable);

    void activate(UUID scheduleId);
    void deactivate(UUID scheduleId);
}
