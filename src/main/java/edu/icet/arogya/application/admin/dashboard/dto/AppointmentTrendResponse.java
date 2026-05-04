package edu.icet.arogya.application.admin.dashboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentTrendResponse {
    private String date; // "2026-04-29"
    private long totalAppointments;
    private long completedAppointments;
    private long cancelledAppointments;
}
