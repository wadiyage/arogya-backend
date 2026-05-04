package edu.icet.arogya.application.admin.location.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateLocationRequest {
    private String name;
    private String address;

    private String city;
    private String district;

    private String contactNumber;
}
