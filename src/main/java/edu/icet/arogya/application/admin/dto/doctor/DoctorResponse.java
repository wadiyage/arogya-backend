package edu.icet.arogya.application.admin.dto.doctor;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorResponse {
    private UUID id;

    private UUID userId;
    private String email;

    private String fullName;
    private String specialization;

    private String phoneNumber;

    private String hospitalName;

    private String consultationFee;

    private boolean available;

    private LocalDateTime createdAt;
}
