package edu.icet.arogya.application.admin.doctor.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDoctorRequest {
    private String email;
    private String password;

    private String fullName;
    private String specialization;
    private String licenseNumber;
    private String qualification;
    private String phoneNumber;
    private String hospitalName;
    private String consultationFee;
}
