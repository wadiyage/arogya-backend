package edu.icet.arogya.modules.appointment.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkCancelRequest {
    private List<UUID> appointmentIds;
    private String reason;
}
