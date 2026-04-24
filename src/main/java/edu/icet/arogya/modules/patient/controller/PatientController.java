package edu.icet.arogya.modules.patient.controller;

import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.modules.patient.dto.UpdatePatientRequest;
import edu.icet.arogya.modules.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/me")
    public ResponseEntity<PatientDetailsResponse> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(
                patientService.getMyProfile(authentication.getName())
        );
    }

    @PutMapping("/me")
    public ResponseEntity<PatientDetailsResponse> updateMyProfile(Authentication authentication, @RequestBody UpdatePatientRequest request) {
        return ResponseEntity.ok(
                patientService.updateMyProfile(authentication.getName(), request)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPatient(@PathVariable UUID id, @RequestParam(defaultValue = "basic") String view) {
        if (view.equals("details")) {
            return ResponseEntity.ok(patientService.getPatientDetails(id));
        }
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }

}
