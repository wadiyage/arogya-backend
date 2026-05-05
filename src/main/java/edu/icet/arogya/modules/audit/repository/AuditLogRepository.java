package edu.icet.arogya.modules.audit.repository;

import edu.icet.arogya.modules.audit.entity.AuditLog;
import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<@NonNull AuditLog, @NonNull UUID> {
    Page<@NonNull AuditLog> findByUserId(UUID userId, Pageable pageable);

    @Query("""
            SELECT a FROM AuditLog a
            WHERE (:userRole IS NULL OR a.userRole = :userRole)
            AND (:action IS NULL OR a.action = :action)
            AND (:entityType IS NULL OR a.entityType = :entityType)
            AND (:userId IS NULL OR a.userId = :userId)
            AND (:entityId IS NULL OR a.entityId = :entityId)
            AND (:from IS NULL OR a.createdAt >= :from)
            AND (:to IS NULL OR a.createdAt <= :to)
            """)
    Page<@NonNull AuditLog> findByFilters(
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
