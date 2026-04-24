package edu.icet.arogya.modules.patient.controller;

import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.UpdatePatientRequest;
import edu.icet.arogya.modules.patient.service.PatientService;
import edu.icet.arogya.security.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@PreAuthorize("hasRole('PATIENT')")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/me")
    public ResponseEntity<@NonNull PatientDetailsResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                patientService.getMyProfile(user.getId())
        );
    }

    @PutMapping("/me")
    public ResponseEntity<@NonNull PatientDetailsResponse> updateMyProfile(@AuthenticationPrincipal UserPrincipal user, @RequestBody UpdatePatientRequest request) {
        return ResponseEntity.ok(
                patientService.updateMyProfile(user.getId(), request)
        );
    }
}
