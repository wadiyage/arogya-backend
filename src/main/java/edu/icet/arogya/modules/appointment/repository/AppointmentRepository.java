package edu.icet.arogya.modules.appointment.repository;

import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.patient.entity.Patient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends
        JpaRepository<@NonNull Appointment,@NonNull UUID>,
        JpaSpecificationExecutor<@NonNull Appointment>
{
    List<Appointment> findByDoctorAndAppointmentDate(
            Doctor doctor,
            LocalDate appointmentDate
    );

    Page<@NonNull Appointment> findByPatient(Patient patient, Pageable pageable);

    List<Appointment> findByDoctorAndStatus(
            Doctor doctor,
            AppointmentStatus status
    );

    List<Appointment> findByDoctorAndAppointmentDateBetween(
            Doctor doctor,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Appointment> findByDoctorAndAppointmentDateAndStatusIn(
            Doctor doctor,
            LocalDate appointmentDate,
            List<AppointmentStatus> statuses
    );

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM Appointment a
            WHERE a.doctor = :doctor
                AND a.appointmentDate = :date
                AND a.status IN :statuses
                AND (
                (:startTime < a.endTime AND :endTime > a.startTime)
                )
            """)
    boolean existsOverlappingAppointment(
            Doctor doctor,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            List<AppointmentStatus> statuses
    );


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
                SUM(CASE WHEN a.status = edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus.COMPLETED THEN 1 ELSE 0 END),
                SUM(CASE WHEN a.status = edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus.CANCELLED THEN 1 ELSE 0 END)
            FROM Appointment a
            WHERE a.appointmentDate BETWEEN :start AND :end
            GROUP BY a.appointmentDate
            ORDER BY a.appointmentDate
            """)
    List<Object[]> findDailyAppointmentTrendsWithStatuses(LocalDate start, LocalDate end);


    @Query("""
            SELECT
                a.doctor.id,
                a.doctor.user.fullName,
                COUNT(a),
                SUM(CASE WHEN a.status = edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus.COMPLETED THEN 1 ELSE 0 END)
            FROM Appointment a
            WHERE a.appointmentDate = :date
            GROUP BY a.doctor.id, a.doctor.user.fullName
            """)
    List<Object[]> findDoctorWorkload(LocalDate date);


    @Query("""
            SELECT a.appointmentDate, COUNT(a)
            FROM Appointment a
            WHERE a.status = edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus.CANCELLED
                AND a.appointmentDate BETWEEN :start AND :end
            GROUP BY a.appointmentDate
            ORDER BY a.appointmentDate
            """)
    List<Object[]> findDailyCancellations(LocalDate start, LocalDate end);


    @Query("""
            SELECT a.appointmentDate, COUNT(a)
            FROM Appointment a
            WHERE a.status = edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus.NO_SHOW
                AND a.appointmentDate BETWEEN :start AND :end
            GROUP BY a.appointmentDate
            ORDER BY a.appointmentDate
            """)
    List<Object[]> findDailyNoShows(LocalDate start, LocalDate end);
}
