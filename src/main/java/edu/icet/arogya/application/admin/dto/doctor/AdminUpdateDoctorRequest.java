package edu.icet.arogya.application.admin.dto.doctor;

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
