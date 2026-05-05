package edu.icet.arogya.modules.audit.entity;

import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.user.entity.Role;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_entity", columnList = "entityType, entityId"),
        @Index(name = "idx_audit_user", columnList = "userId"),
        @Index(name = "idx_audit_created", columnList = "createdAt")
})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName userRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditEntityType entityType;

    @Column(nullable = false)
    private UUID entityId;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
