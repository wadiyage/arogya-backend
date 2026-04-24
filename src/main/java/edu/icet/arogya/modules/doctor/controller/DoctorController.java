package edu.icet.arogya.modules.doctor.controller;

import edu.icet.arogya.modules.doctor.dto.DoctorDetailsResponse;
import edu.icet.arogya.modules.doctor.dto.DoctorSelfUpdateRequest;
import edu.icet.arogya.modules.doctor.service.DoctorService;
import edu.icet.arogya.security.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/me")
    public ResponseEntity<@NonNull DoctorDetailsResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(doctorService.getMyProfile(user.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<@NonNull DoctorDetailsResponse> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody DoctorSelfUpdateRequest request
    ) {
        return ResponseEntity.ok(doctorService.updateMyProfile(user.getId(), request));
    }
}
