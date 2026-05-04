package edu.icet.arogya.application.admin.schedule.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateScheduleRequest {
    private UUID doctorId;
    private UUID locationId;
    private LocalDate date;
    private Integer maxTokens;
    private String sessionName;
    private Double consultationFee;
}
