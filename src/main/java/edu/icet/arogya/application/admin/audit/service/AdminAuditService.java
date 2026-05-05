package edu.icet.arogya.application.admin.audit.service;

import edu.icet.arogya.application.admin.audit.dto.AuditLogFilterRequest;
import edu.icet.arogya.application.admin.audit.dto.AuditLogResponse;
import edu.icet.arogya.application.admin.audit.dto.CreateAuditLogRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminAuditService {
    void log(CreateAuditLogRequest request);

    AuditLogResponse getById(UUID id);
    Page<@NonNull AuditLogResponse> getAll(Pageable pageable);
    Page<@NonNull AuditLogResponse> getByUser(UUID userId, Pageable pageable);

    Page<@NonNull AuditLogResponse> filter(AuditLogFilterRequest filter, Pageable pageable);
}
