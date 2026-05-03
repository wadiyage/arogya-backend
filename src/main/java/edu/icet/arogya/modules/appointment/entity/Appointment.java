package edu.icet.arogya.modules.appointment.entity;

import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "appointments",
        indexes = {
                @Index(name = "idx_patient", columnList = "patient_id"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_deleted", columnList = "deleted"),
                @Index(name = "idx_schedule_token", columnList = "schedule_id, token_number")
        }
)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private DoctorSchedule schedule;

    @Column(name = "token_number", nullable = false)
    private Integer tokenNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(length = 500)
    private String reason;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private boolean deleted = false;

    private LocalDateTime deletedAt;

    private LocalDateTime checkInTime; // When patient checks in for the appointment
    private LocalDateTime consultationStartTime; // When consultation starts
    private LocalDateTime consultationEndTime; // When consultation ends

    private LocalDateTime cancelledAt;
    private LocalDateTime completedAt;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        if(status == null) {
            status = AppointmentStatus.CONFIRMED;
        }
        deleted = false;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
