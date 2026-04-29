package edu.icet.arogya.application.admin.service;

import edu.icet.arogya.application.admin.dto.dashboard.*;

import java.time.LocalDate;
import java.util.List;

public interface AdminDashboardService {
    DashboardOverviewResponse getOverview();
    List<AppointmentTrendResponse> getTrends(int days);
    List<DoctorWorkloadResponse> getDoctorWorkload(LocalDate date);
    List<CancellationSummaryResponse> getCancellations(int days);
    List<NoShowSummaryResponse> getNoShows(int days);
}
