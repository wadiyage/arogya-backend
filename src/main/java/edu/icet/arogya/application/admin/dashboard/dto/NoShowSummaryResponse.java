package edu.icet.arogya.application.admin.dashboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoShowSummaryResponse {
    private String date;
    private long noShows;
}
