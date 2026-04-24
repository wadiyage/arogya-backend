package edu.icet.arogya.modules.medicalrecord.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordSummary {
    private UUID id;

    private UUID doctorId;
    private String doctorName;

    private String symptoms;

    private String notes;

    private LocalDateTime visitedAt;
}
