package edu.icet.arogya.application.admin.location.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationResponse {
    private UUID id;

    private String name;
    private String address;

    private String city;
    private String district;

    private String contactNumber;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
