package edu.icet.arogya.modules.appointment.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAppointmentRequest {
    private UUID scheduleId;

    private String reason;
    private String notes;

    private Boolean walkIn;
}
