package edu.icet.arogya.modules.appointment.dto;

import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminStatusUpdateRequest {
    private AppointmentStatus status;
    private String reason;
}
