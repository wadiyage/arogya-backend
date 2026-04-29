package edu.icet.arogya.application.admin.dto.dashboard;

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
