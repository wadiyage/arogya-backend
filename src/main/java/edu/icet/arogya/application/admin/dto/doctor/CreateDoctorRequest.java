package edu.icet.arogya.application.admin.dto.doctor;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDoctorRequest {
    private UUID userId;
    private String fullName;
    private String specialization;
    private String licenseNumber;
    private String qualifications;
    private String phoneNumber;
    private String hospitalName;
    private String consultationFee;
}
