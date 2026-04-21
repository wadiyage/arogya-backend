package edu.icet.arogya.modules.medicalrecord.controller;

import edu.icet.arogya.modules.medicalrecord.dto.CreateMedicalRecordRequest;
import edu.icet.arogya.modules.medicalrecord.dto.MedicalRecordResponse;
import edu.icet.arogya.modules.medicalrecord.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordResponse> create(@RequestBody CreateMedicalRecordRequest request, Authentication authentication) {
        return ResponseEntity.ok(
                medicalRecordService.createMedicalRecord(request, authentication)
        );
    }
}
