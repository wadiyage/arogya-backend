package edu.icet.arogya.modules.patient.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePatientRequest {
    private UUID userId;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String phoneNumber;
    private String address;
    private String emergencyContactName;
    private String emergencyContactNumber;
}
