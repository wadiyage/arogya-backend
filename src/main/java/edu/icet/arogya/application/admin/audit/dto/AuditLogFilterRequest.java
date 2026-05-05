package edu.icet.arogya.application.admin.audit.dto;

import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogFilterRequest {
    private RoleName userRole;
    private AuditAction action;
    private AuditEntityType entityType;

    private UUID userId;
    private UUID entityId;

    private LocalDateTime from;
    private LocalDateTime to;
}
