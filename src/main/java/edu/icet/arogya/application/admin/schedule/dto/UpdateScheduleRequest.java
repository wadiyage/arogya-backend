package edu.icet.arogya.application.admin.schedule.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateScheduleRequest {
    private LocalDate date;
    private Integer maxTokens;
    private String sessionName;
    private Double consultationFee;
}
