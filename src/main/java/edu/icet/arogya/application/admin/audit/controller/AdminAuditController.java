package edu.icet.arogya.application.admin.audit.controller;

import edu.icet.arogya.application.admin.audit.dto.AuditLogFilterRequest;
import edu.icet.arogya.application.admin.audit.dto.AuditLogResponse;
import edu.icet.arogya.application.admin.audit.service.AdminAuditService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuditController {

    private final AdminAuditService auditService;

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull AuditLogResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(auditService.getById(id));
    }

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull AuditLogResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(auditService.getAll(pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<@NonNull Page<@NonNull AuditLogResponse>> getByUser(@PathVariable UUID userId, Pageable pageable) {
        return ResponseEntity.ok(auditService.getByUser(userId, pageable));
    }

    @PostMapping("/filter")
    public ResponseEntity<@NonNull Page<@NonNull AuditLogResponse>> filter(@RequestBody AuditLogFilterRequest filter, Pageable pageable) {
        return ResponseEntity.ok(auditService.filter(filter, pageable));
    }
}
