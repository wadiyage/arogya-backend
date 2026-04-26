package edu.icet.arogya.application.admin.controller;

import edu.icet.arogya.application.admin.dto.patient.AdminUpdatePatientRequest;
import edu.icet.arogya.application.admin.service.AdminPatientService;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<@NonNull PatientDetailsResponse> updatePatient(@PathVariable UUID id, @RequestBody AdminUpdatePatientRequest request) {
        return ResponseEntity.ok(adminPatientService.updatePatient(id, request));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<@NonNull Void> deactivatePatient(@PathVariable UUID id) {
        adminPatientService.deactivatePatient(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<@NonNull Void> activatePatient(@PathVariable UUID id) {
        adminPatientService.activatePatient(id);
        return ResponseEntity.noContent().build();
    }
}
