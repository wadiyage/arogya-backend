package edu.icet.arogya.modules.patient.dto;

import edu.icet.arogya.modules.patient.entity.enums.BloodGroup;
import edu.icet.arogya.modules.patient.entity.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePatientRequest {
    private UUID userId;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private BloodGroup bloodGroup;
    private String phoneNumber;
    private String address;
    private String emergencyContactName;
    private String emergencyContactNumber;
}
