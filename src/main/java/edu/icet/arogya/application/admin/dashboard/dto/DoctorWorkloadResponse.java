package edu.icet.arogya.application.admin.dashboard.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorWorkloadResponse {
    private UUID doctorId;
    private String doctorName;

    private long totalAppointments;
    private long completedAppointments;
}
