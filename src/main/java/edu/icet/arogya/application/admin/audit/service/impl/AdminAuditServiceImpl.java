package edu.icet.arogya.application.admin.audit.service.impl;

import edu.icet.arogya.application.admin.audit.dto.AuditLogFilterRequest;
import edu.icet.arogya.application.admin.audit.dto.AuditLogResponse;
import edu.icet.arogya.application.admin.audit.dto.CreateAuditLogRequest;
import edu.icet.arogya.application.admin.audit.service.AdminAuditService;
import edu.icet.arogya.modules.audit.entity.AuditLog;
import edu.icet.arogya.modules.audit.mapper.AuditLogMapper;
import edu.icet.arogya.modules.audit.service.AuditService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuditServiceImpl implements AdminAuditService {

    private final AuditService auditService;

    private final AuditLogMapper auditLogMapper;

    @Override
    public void log(CreateAuditLogRequest request) {
        auditService.log(
                request.getUserId(),
                request.getUserRole(),
                request.getAction(),
                request.getEntityType(),
                request.getEntityId(),
                request.getDescription(),
                request.getMetadata()
        );
    }

    @Override
    public AuditLogResponse getById(UUID id) {
        AuditLog exiting = auditService.getById(id);
        return auditLogMapper.mapToResponse(exiting);
    }

    @Override
    public Page<@NonNull AuditLogResponse> getAll(Pageable pageable) {
        Page<@NonNull AuditLog> logs = auditService.getAll(pageable);
        return logs.map(auditLogMapper::mapToResponse);
    }

    @Override
    public Page<@NonNull AuditLogResponse> getByUser(UUID userId, Pageable pageable) {
        Page<@NonNull AuditLog> logs = auditService.getByUser(userId, pageable);
        return logs.map(auditLogMapper::mapToResponse);
    }

    @Override
    public Page<@NonNull AuditLogResponse> filter(AuditLogFilterRequest filter, Pageable pageable) {
        Page<@NonNull AuditLog> logs = auditService.filter(
                filter.getUserRole(),
                filter.getAction(),
                filter.getEntityType(),
                filter.getUserId(),
                filter.getEntityId(),
                filter.getFrom(),
                filter.getTo(),
                pageable
        );
        return logs.map(auditLogMapper::mapToResponse);
    }
}
