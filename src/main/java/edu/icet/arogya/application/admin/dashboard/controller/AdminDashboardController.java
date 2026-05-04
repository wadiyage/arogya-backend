package edu.icet.arogya.application.admin.dashboard.controller;

import edu.icet.arogya.application.admin.dashboard.dto.*;
import edu.icet.arogya.application.admin.dashboard.service.AdminDashboardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<@NonNull DashboardOverviewResponse> getOverview() {
        return ResponseEntity.ok(dashboardService.getOverview());
    }

    @GetMapping("/trends")
    public ResponseEntity<@NonNull List<@NonNull AppointmentTrendResponse>> getTrends(@RequestParam int days) {
        return ResponseEntity.ok(dashboardService.getTrends(days));
    }

    @GetMapping("/doctor-workload")
    public ResponseEntity<@NonNull List<@NonNull DoctorWorkloadResponse>> getDoctorWorkload(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(dashboardService.getDoctorWorkload(date));
    }

    @GetMapping("/cancellations")
    public ResponseEntity<@NonNull List<@NonNull CancellationSummaryResponse>> getCancellations(@RequestParam int days) {
        return ResponseEntity.ok(dashboardService.getCancellations(days));
    }

    @GetMapping("/no-shows")
    public ResponseEntity<@NonNull List<@NonNull NoShowSummaryResponse>> getNoShows(@RequestParam int days) {
        return ResponseEntity.ok(dashboardService.getNoShows(days));
    }
}
