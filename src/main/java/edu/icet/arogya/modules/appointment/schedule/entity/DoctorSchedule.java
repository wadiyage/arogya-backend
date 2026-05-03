package edu.icet.arogya.modules.appointment.schedule.entity;

import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.location.entity.Location;
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
        name = "doctor_schedules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_doctor_location_date",
                        columnNames = {"doctor_id", "location_id", "schedule_date"}
                )
        },
        indexes = {
                @Index(name = "idx_doctor_schedule", columnList = "doctor_id, schedule_date"),
                @Index(name = "idx_location_schedule", columnList = "location_id, schedule_date")
        }
)
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @Column(nullable = false)
    private Integer maxTokens;

    @Column(nullable = false)
    private Integer bookedTokens = 0;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(length = 50)
    private String sessionName;

    @Column(nullable = false)
    private Double consultationFee;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (bookedTokens == null) bookedTokens = 0;
        if(active == null) active = true;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
