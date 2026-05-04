package edu.icet.arogya.modules.appointment.schedule.repository;

import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.location.entity.Location;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DoctorScheduleRepository extends JpaRepository<@NonNull DoctorSchedule, @NonNull UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.id = :id")
    Optional<@NonNull DoctorSchedule> findByIdForUpdate(UUID id);

    boolean existsByDoctorAndLocationAndScheduleDate(
            Doctor doctor,
            Location location,
            LocalDate date
    );

    Page<@NonNull DoctorSchedule> findByDoctorIdAndScheduleDate(UUID doctorId, LocalDate date, Pageable pageable);

    Page<@NonNull DoctorSchedule> findByLocationIdAndScheduleDate(UUID locationId, LocalDate date, Pageable pageable);
}
