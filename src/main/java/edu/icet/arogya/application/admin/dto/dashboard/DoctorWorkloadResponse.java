package edu.icet.arogya.application.admin.dto.dashboard;

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
