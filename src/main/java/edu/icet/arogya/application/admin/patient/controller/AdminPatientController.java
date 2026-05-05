package edu.icet.arogya.application.admin.patient.controller;

import edu.icet.arogya.application.admin.patient.dto.AdminUpdatePatientRequest;
import edu.icet.arogya.application.admin.patient.service.AdminPatientService;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.security.user.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/patients")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminPatientController {

    private final AdminPatientService adminPatientService;

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull PatientDetailsResponse> getPatientDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(adminPatientService.getPatientDetails(id));
    }

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull PatientResponse>> getAllPatients(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
            ) {
        return ResponseEntity.ok(adminPatientService.getAllPatients(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull PatientDetailsResponse> updatePatient(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID id, @RequestBody AdminUpdatePatientRequest request) {
        return ResponseEntity.ok(adminPatientService.updatePatient(user, id, request));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<@NonNull Void> deactivatePatient(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID id) {
        adminPatientService.deactivatePatient(user, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<@NonNull Void> activatePatient(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID id) {
        adminPatientService.activatePatient(user, id);
        return ResponseEntity.noContent().build();
    }
}
