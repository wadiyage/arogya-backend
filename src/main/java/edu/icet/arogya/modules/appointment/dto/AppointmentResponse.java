package edu.icet.arogya.modules.appointment.dto;

import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDate;
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

    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private AppointmentStatus status;

    private String reason;
    private String notes;
}
