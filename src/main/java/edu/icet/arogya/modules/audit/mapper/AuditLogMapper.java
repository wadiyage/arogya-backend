package edu.icet.arogya.modules.audit.mapper;

import edu.icet.arogya.application.admin.audit.dto.AuditLogResponse;
import edu.icet.arogya.modules.audit.entity.AuditLog;

public class AuditLogMapper {
    public AuditLogResponse mapToResponse(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .userId(log.getUserId())
                .userRole(log.getUserRole())
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .description(log.getDescription())
                .metadata(log.getMetadata())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
