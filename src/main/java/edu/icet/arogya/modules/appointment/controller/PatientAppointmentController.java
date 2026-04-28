package edu.icet.arogya.modules.appointment.controller;

import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.dto.CreateAppointmentRequest;
import edu.icet.arogya.modules.appointment.service.PatientAppointmentService;
import edu.icet.arogya.security.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patient/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PATIENT')")
public class PatientAppointmentController {

    private final PatientAppointmentService patientAppointmentService;

    @PostMapping
    public ResponseEntity<@NonNull AppointmentResponse> bookAppointment(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CreateAppointmentRequest request
    ) {
        return ResponseEntity.ok(patientAppointmentService.bookAppointment(user.getId(), request));
    }

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull AppointmentResponse>> getMyAppointments(@AuthenticationPrincipal UserPrincipal user, Pageable pageable) {
        return ResponseEntity.ok(patientAppointmentService.getMyAppointments(user.getId(), pageable));
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<@NonNull Void> cancelAppointment(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable UUID appointmentId
    ) {
        patientAppointmentService.cancelAppointment(user.getId(), appointmentId);
        return ResponseEntity.noContent().build();
    }
}
