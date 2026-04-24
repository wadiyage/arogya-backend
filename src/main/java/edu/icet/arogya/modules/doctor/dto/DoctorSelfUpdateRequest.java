package edu.icet.arogya.modules.doctor.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorSelfUpdateRequest {
    private String fullName;
    private String qualification;
    private String phoneNumber;
    private Boolean available;
}
