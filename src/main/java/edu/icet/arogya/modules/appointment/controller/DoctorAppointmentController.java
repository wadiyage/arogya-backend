package edu.icet.arogya.modules.appointment.controller;

import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.service.DoctorAppointmentService;
import edu.icet.arogya.security.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorAppointmentController {

    private final DoctorAppointmentService doctorAppointmentService;

    @GetMapping
    public ResponseEntity<@NonNull List<AppointmentResponse>> getMySchedule(
            @AuthenticationPrincipal UserPrincipal user
            ) {
        return ResponseEntity.ok(doctorAppointmentService.getMySchedule(user.getId()));
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<@NonNull AppointmentResponse> updateAppointmentStatus(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable UUID appointmentId,
            @RequestParam AppointmentStatus status
    ) {
        return ResponseEntity.ok(doctorAppointmentService.updateAppointmentStatus(
                user.getId(),
                appointmentId,
                status
        ));
    }
}
