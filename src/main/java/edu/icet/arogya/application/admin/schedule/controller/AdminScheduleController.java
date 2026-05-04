package edu.icet.arogya.application.admin.schedule.controller;

import edu.icet.arogya.application.admin.schedule.dto.CreateScheduleRequest;
import edu.icet.arogya.application.admin.schedule.dto.ScheduleResponse;
import edu.icet.arogya.application.admin.schedule.dto.UpdateScheduleRequest;
import edu.icet.arogya.application.admin.schedule.service.AdminScheduleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/schedules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminScheduleController {

    private final AdminScheduleService adminScheduleService;

    @PostMapping
    public ResponseEntity<@NonNull ScheduleResponse> create(@RequestBody CreateScheduleRequest request) {
        return ResponseEntity.ok(adminScheduleService.create(request));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<@NonNull ScheduleResponse> update(@PathVariable UUID scheduleId, @RequestBody UpdateScheduleRequest request) {
        return ResponseEntity.ok(adminScheduleService.update(scheduleId, request));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<@NonNull ScheduleResponse> getById(@PathVariable UUID scheduleId) {
        return ResponseEntity.ok(adminScheduleService.getById(scheduleId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<@NonNull Page<@NonNull ScheduleResponse>> getByDoctor(@PathVariable UUID doctorId, @RequestParam LocalDate date, Pageable pageable) {
        return ResponseEntity.ok(adminScheduleService.getByDoctor(doctorId, date, pageable));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<@NonNull Page<@NonNull ScheduleResponse>> getByLocation(@PathVariable UUID locationId, @RequestParam LocalDate date, Pageable pageable) {
        return ResponseEntity.ok(adminScheduleService.getByLocation(locationId, date, pageable));
    }

    @PatchMapping("/{scheduleId}/activate")
    public ResponseEntity<@NonNull ScheduleResponse> activate(@PathVariable UUID scheduleId) {
        adminScheduleService.activate(scheduleId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{scheduleId}/deactivate")
    public ResponseEntity<@NonNull ScheduleResponse> deactivate(@PathVariable UUID scheduleId) {
        adminScheduleService.deactivate(scheduleId);
        return ResponseEntity.ok().build();
    }
}
