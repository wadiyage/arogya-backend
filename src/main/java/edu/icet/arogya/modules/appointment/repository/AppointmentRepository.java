package edu.icet.arogya.modules.appointment.repository;

import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.patient.entity.Patient;
import lombok.NonNull;
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

    List<Appointment> findByPatient(Patient patient);

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
}
