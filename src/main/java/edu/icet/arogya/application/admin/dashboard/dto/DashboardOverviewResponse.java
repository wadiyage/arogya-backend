package edu.icet.arogya.application.admin.dashboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardOverviewResponse {
    private long totalAppointments;
    private long todayAppointments;

    private long confirmedAppointments;
    private long completedAppointments;
    private long cancelledAppointments;

    private long noShowAppointments;

    private long activeDoctors;
}
