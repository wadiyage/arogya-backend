package edu.icet.arogya.modules.appointment.audit.dto;

import edu.icet.arogya.modules.appointment.audit.entity.enums.AuditActionType;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentAuditLogRequest {
    private Appointment appointment;

    private AppointmentStatus oldStatus;
    private AppointmentStatus newStatus;

    private AuditActionType actionType;

    private String reason;

    private UUID userId;
    private RoleName userRole;

    private String metadata;
}
