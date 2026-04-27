package edu.icet.arogya.application.admin.controller;

import edu.icet.arogya.application.admin.service.AdminAppointmentService;
import edu.icet.arogya.modules.appointment.dto.AdminStatusUpdateRequest;
import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.dto.BulkCancelRequest;
import edu.icet.arogya.modules.appointment.dto.filter.AppointmentFilterRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAppointmentController {

    private final AdminAppointmentService adminAppointmentService;

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull AppointmentResponse>> getAllAppointments(
            @ModelAttribute AppointmentFilterRequest filter,
            Pageable pageable
    ) {
        return ResponseEntity.ok(adminAppointmentService.getAllAppointments(filter, pageable));
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<@NonNull AppointmentResponse> overrideStatus(
            @PathVariable UUID appointmentId,
            @RequestBody AdminStatusUpdateRequest request
            ) {
        return ResponseEntity.ok(adminAppointmentService.overrideStatus(
                appointmentId,
                request.getStatus(),
                request.getReason()
        ));
    }

    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<@NonNull Void> cancelAppointmentByAdmin(
            @PathVariable UUID appointmentId
    ) {
        adminAppointmentService.cancelAppointmentByAdmin(appointmentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/bulk-cancel")
    public ResponseEntity<@NonNull Void> bulkCancelAppointmentsByAdmin(@RequestBody BulkCancelRequest request) {
        adminAppointmentService.bulkCancelAppointmentsByAdmin(
                request.getAppointmentIds(),
                request.getReason()
        );
        return ResponseEntity.noContent().build();
    }
}
