package edu.icet.arogya.modules.audit.service.impl;

import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.application.admin.audit.dto.AuditLogFilterRequest;
import edu.icet.arogya.application.admin.audit.dto.AuditLogResponse;
import edu.icet.arogya.application.admin.audit.dto.CreateAuditLogRequest;
import edu.icet.arogya.modules.audit.entity.AuditLog;
import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.audit.mapper.AuditLogMapper;
import edu.icet.arogya.modules.audit.repository.AuditLogRepository;
import edu.icet.arogya.modules.audit.service.AuditService;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(
            UUID userId,
            RoleName userRole,
            AuditAction action,
            AuditEntityType entityType,
            UUID entityId,
            String description,
            String metadata
    ) {
        AuditLog log = AuditLog.builder()
                .userId(userId)
                .userRole(userRole)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .description(description)
                .metadata(metadata)
                .build();

        auditLogRepository.save(log);
    }

    @Override
    public AuditLog getById(UUID id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with ID: " + id));
    }

    @Override
    public Page<@NonNull AuditLog> getAll(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    @Override
    public Page<@NonNull AuditLog> getByUser(UUID userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<@NonNull AuditLog> filter(
            RoleName userRole,
            AuditAction action,
            AuditEntityType entityType,
            UUID userId,
            UUID entityId,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    ) {
        return auditLogRepository.findByFilters(
                userRole,
                action,
                entityType,
                userId,
                entityId,
                from,
                to,
                pageable
        );
    }
}
