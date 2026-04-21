package edu.icet.arogya.modules.patient.dto;

import edu.icet.arogya.modules.medicalrecord.dto.MedicalRecordSummary;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDetailsResponse {
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

    private List<MedicalRecordSummary> medicalRecords;
}
