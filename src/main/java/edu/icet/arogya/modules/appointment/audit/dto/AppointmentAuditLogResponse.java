package edu.icet.arogya.modules.appointment.audit.dto;

import edu.icet.arogya.modules.appointment.audit.entity.enums.AuditActionType;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentAuditLogResponse {
    private UUID id;
    private UUID appointmentId;

    private AuditActionType actionType;

    private AppointmentStatus oldStatus;
    private AppointmentStatus newStatus;

    private String reason;

    private UUID changedByUserId;
    private RoleName changedByRole;

    private String metadata;

    private LocalDateTime changedAt;
}
