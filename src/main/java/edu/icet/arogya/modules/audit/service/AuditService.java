package edu.icet.arogya.modules.audit.service;

import edu.icet.arogya.application.admin.audit.dto.AuditLogFilterRequest;
import edu.icet.arogya.application.admin.audit.dto.AuditLogResponse;
import edu.icet.arogya.application.admin.audit.dto.CreateAuditLogRequest;
import edu.icet.arogya.modules.audit.entity.AuditLog;
import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AuditService {
    void log(
            UUID userId,
            RoleName userRole,
            AuditAction action,
            AuditEntityType entityType,
            UUID entityId,
            String description,
            String metadata
    );

    AuditLog getById(UUID id);
    Page<@NonNull AuditLog> getAll(Pageable pageable);
    Page<@NonNull AuditLog> getByUser(UUID userId, Pageable pageable);

    Page<@NonNull AuditLog> filter(
            RoleName userRole,
            AuditAction action,
            AuditEntityType entityType,
            UUID userId,
            UUID entityId,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );
}
