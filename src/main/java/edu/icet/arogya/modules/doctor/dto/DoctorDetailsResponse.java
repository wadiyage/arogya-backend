package edu.icet.arogya.modules.doctor.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDetailsResponse {
    private UUID id;

    private UUID userId;
    private String email;

    private String fullName;
    private String specialization;

    private String licenseNumber;
    private String qualifications;

    private String phoneNumber;

    private String hospitalName;

    private String consultationFee;

    private boolean available;

    private LocalDateTime createdAt;
}
