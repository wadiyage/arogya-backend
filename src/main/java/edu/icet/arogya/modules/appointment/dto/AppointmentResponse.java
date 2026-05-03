package edu.icet.arogya.modules.appointment.dto;

import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
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
public class AppointmentResponse {
    private UUID id;

    private UUID patientId;
    private String patientName;

    private UUID doctorId;
    private String doctorName;

    private UUID scheduleId;

    private String locationName;

    private LocalDate scheduleDate;

    private Integer tokenNumber;

    private AppointmentStatus status;

    private String reason;
    private String notes;

    private LocalDateTime checkInTime;
    private LocalDateTime consultationStartTime;
    private LocalDateTime consultationEndTime;

    private LocalDateTime createdAt;
}
