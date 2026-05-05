package edu.icet.arogya.application.admin.audit.dto;

import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAuditLogRequest {
    private UUID userId;
    private RoleName userRole;

    private AuditAction action;
    private AuditEntityType entityType;

    private UUID entityId;

    private String description;
    private String metadata;
}
