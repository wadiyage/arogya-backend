package edu.icet.arogya.application.admin.dashboard.service;

import edu.icet.arogya.application.admin.dashboard.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface AdminDashboardService {
    DashboardOverviewResponse getOverview();
    List<AppointmentTrendResponse> getTrends(int days);
    List<DoctorWorkloadResponse> getDoctorWorkload(LocalDate date);
    List<CancellationSummaryResponse> getCancellations(int days);
    List<NoShowSummaryResponse> getNoShows(int days);
}
