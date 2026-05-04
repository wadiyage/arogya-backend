package edu.icet.arogya.application.admin.patient.dto;

import edu.icet.arogya.modules.patient.entity.enums.BloodGroup;
import edu.icet.arogya.modules.patient.entity.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUpdatePatientRequest {
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private BloodGroup bloodGroup;
    private String phoneNumber;
    private String address;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private Boolean active;
}
