package edu.icet.arogya.application.admin.dashboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancellationSummaryResponse {
    private String date;
    private long cancellations;
}
