package edu.icet.arogya.application.admin.controller;

import edu.icet.arogya.application.admin.dto.doctor.AdminUpdateDoctorRequest;
import edu.icet.arogya.application.admin.dto.doctor.CreateDoctorRequest;
import edu.icet.arogya.application.admin.dto.doctor.DoctorResponse;
import edu.icet.arogya.application.admin.service.AdminDoctorService;
import edu.icet.arogya.modules.doctor.dto.DoctorDetailsResponse;
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
@RequestMapping("/api/admin/doctors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDoctorController {

    private final AdminDoctorService adminDoctorService;

    @PostMapping
    public ResponseEntity<@NonNull DoctorResponse> createDoctor(@RequestBody CreateDoctorRequest request) {
        return ResponseEntity.ok(adminDoctorService.createDoctor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull DoctorDetailsResponse> updateDoctor(
            @PathVariable UUID id,
            @RequestBody AdminUpdateDoctorRequest request
    ) {
        return ResponseEntity.ok(adminDoctorService.updateDoctor(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<@NonNull Void> deactivateDoctor(
            @PathVariable UUID id,
            @RequestParam boolean available
    ) {
        adminDoctorService.deactivateDoctor(id, available);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull DoctorDetailsResponse> getDoctorDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(adminDoctorService.getDoctorDetails(id));
    }

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull DoctorResponse>> getAllDoctors(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(adminDoctorService.getAllDoctors(pageable));
    }
}
