package edu.icet.arogya.modules.patient.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse {
    private UUID id;

    private UUID userId;
    private String email;

    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;

    private String phoneNumber;
    private String address;

    private boolean isActive;

    private LocalDateTime createdAt;
}
