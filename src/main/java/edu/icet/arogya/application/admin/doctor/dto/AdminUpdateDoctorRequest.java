package edu.icet.arogya.application.admin.doctor.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUpdateDoctorRequest {
    private String specialization;
    private String licenseNumber;
    private String hospitalName;
    private String consultationFee;
    private Boolean available;
}
