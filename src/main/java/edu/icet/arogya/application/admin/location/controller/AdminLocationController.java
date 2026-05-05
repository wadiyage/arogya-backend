package edu.icet.arogya.application.admin.location.controller;

import edu.icet.arogya.application.admin.location.dto.CreateLocationRequest;
import edu.icet.arogya.application.admin.location.dto.LocationResponse;
import edu.icet.arogya.application.admin.location.dto.UpdateLocationRequest;
import edu.icet.arogya.application.admin.location.service.AdminLocationService;
import edu.icet.arogya.security.user.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/locations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminLocationController {

    private final AdminLocationService adminLocationService;

    @PostMapping
    public ResponseEntity<@NonNull LocationResponse> create(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CreateLocationRequest request
    ) {
        return ResponseEntity.ok(adminLocationService.create(user, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull LocationResponse> update(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable UUID id,
            @RequestBody UpdateLocationRequest request) {
        return ResponseEntity.ok(adminLocationService.update(user, id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull LocationResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminLocationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<@NonNull Page<@NonNull LocationResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(adminLocationService.getAll(pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<@NonNull Page<@NonNull LocationResponse>> getActiveLocations(Pageable pageable) {
        return ResponseEntity.ok(adminLocationService.getActiveLocations(pageable));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<@NonNull Void> activate(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID id) {
        adminLocationService.activate(user, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<@NonNull Void> deactivate(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID id) {
        adminLocationService.deactivate(user, id);
        return ResponseEntity.ok().build();
    }
}
