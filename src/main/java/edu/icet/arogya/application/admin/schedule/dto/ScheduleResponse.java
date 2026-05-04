package edu.icet.arogya.application.admin.schedule.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResponse {
    private UUID id;

    private UUID doctorId;
    private String doctorName;

    private UUID locationId;
    private String locationName;

    private LocalDate date;

    private Integer maxTokens;
    private Integer bookedTokens;

    private Boolean active;

    private String sessionName;

    private Double consultationFee;
}
