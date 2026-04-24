package edu.icet.arogya.modules.medicalrecord.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMedicalRecordRequest {
    private UUID patientId;
    private String symptoms;
    private String notes;
}
