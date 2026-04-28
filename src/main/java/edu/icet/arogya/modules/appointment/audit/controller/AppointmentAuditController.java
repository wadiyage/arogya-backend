package edu.icet.arogya.modules.appointment.audit.controller;

import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogResponse;
import edu.icet.arogya.modules.appointment.audit.service.AppointmentAuditService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/appointments/")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AppointmentAuditController {

    private final AppointmentAuditService auditService;

    @GetMapping("{appointmentId}/audit-logs")
    public ResponseEntity<@NonNull Page<@NonNull AppointmentAuditLogResponse>> getLogsByAppointment(
            @PathVariable UUID appointmentId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(auditService.getLogsByAppointment(appointmentId, pageable));
    }
}
