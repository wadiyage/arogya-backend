package edu.icet.arogya.modules.appointment.schedule.repository;

import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface DoctorScheduleRepository extends JpaRepository<@NonNull DoctorSchedule, @NonNull UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.id = :id")
    Optional<@NonNull DoctorSchedule> findByIdForUpdate(UUID id);
}
