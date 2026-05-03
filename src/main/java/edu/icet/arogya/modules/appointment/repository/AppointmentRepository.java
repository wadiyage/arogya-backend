package edu.icet.arogya.modules.appointment.repository;

import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.patient.entity.Patient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends
        JpaRepository<@NonNull Appointment,@NonNull UUID>,
        JpaSpecificationExecutor<@NonNull Appointment>
{
    Page<@NonNull Appointment> findByPatient(Patient patient, Pageable pageable);

    boolean existsByPatientAndScheduleAndDeletedFalse(Patient patient, DoctorSchedule schedule);


    @Query("SELECT COUNT(a) FROM Appointment a")
    long countTotalAppointments();

    @Query("""
            SELECT COUNT(a)
            FROM Appointment a
            WHERE a.appointmentDate = :date
            """)
    long countAppointmentsByDate(LocalDate date);

    @Query("""
            SELECT COUNT(a)
            FROM Appointment a
            WHERE a.status = :status
            """)
    long countAppointmentsByStatus(AppointmentStatus status);


    @Query("""
            SELECT COUNT(a)
            FROM Appointment a
            WHERE a.appointmentDate BETWEEN :start AND :end
            """)
    long countAppointmentsBetweenDates(LocalDate start, LocalDate end);


    @Query("""
            SELECT
                a.appointmentDate,
                COUNT(a),
                SUM(CASE WHEN a.status = AppointmentStatus.COMPLETED THEN 1 ELSE 0 END),
                SUM(CASE WHEN a.status = AppointmentStatus.CANCELLED THEN 1 ELSE 0 END)
            FROM Appointment a
            WHERE a.appointmentDate BETWEEN :start AND :end
            GROUP BY a.appointmentDate
            ORDER BY a.appointmentDate
            """)
    List<Object[]> findDailyAppointmentTrendsWithStatuses(LocalDate start, LocalDate end);


    @Query("""
            SELECT
                a.doctor.id,
                a.doctor.fullName,
                COUNT(a),
                SUM(CASE WHEN a.status = :completedStatus THEN 1 ELSE 0 END)
            FROM Appointment a
            WHERE a.appointmentDate = :date
            GROUP BY a.doctor.id, a.doctor.fullName
            """)
    List<Object[]> findDoctorWorkload(
            LocalDate date,
            AppointmentStatus completedStatus
    );


    @Query("""
            SELECT a.appointmentDate, COUNT(a)
            FROM Appointment a
            WHERE a.status = AppointmentStatus.CANCELLED
                AND a.appointmentDate BETWEEN :start AND :end
            GROUP BY a.appointmentDate
            ORDER BY a.appointmentDate
            """)
    List<Object[]> findDailyCancellations(LocalDate start, LocalDate end);


    @Query("""
            SELECT a.appointmentDate, COUNT(a)
            FROM Appointment a
            WHERE a.status = AppointmentStatus.NO_SHOW
                AND a.appointmentDate BETWEEN :start AND :end
            GROUP BY a.appointmentDate
            ORDER BY a.appointmentDate
            """)
    List<Object[]> findDailyNoShows(LocalDate start, LocalDate end);


    @Query("""
            SELECT a
            FROM Appointment a
            WHERE a.schedule.doctor = :doctor
            AND a.schedule.scheduleDate = :date
            AND a.status IN :statuses
            AND a.deleted = false
            """)
    List<Appointment> findTodayQueue(
            Doctor doctor,
            LocalDate date,
            List<AppointmentStatus> statuses
    );

    @Query("""
            SELECT a
            FROM Appointment a
            WHERE a.schedule.doctor = :doctor
            AND a.schedule.scheduleDate = :date
            AND a.status IN ('CONFIRMED', 'CHECKED_IN')
            AND a.deleted = false
            ORDER BY a.tokenNumber ASC
            """)
    Optional<Appointment> findQueueOrdered(Doctor doctor, LocalDate date);
}
