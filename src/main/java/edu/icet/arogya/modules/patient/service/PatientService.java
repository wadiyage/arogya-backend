package edu.icet.arogya.modules.patient.service;

import edu.icet.arogya.modules.patient.dto.CreatePatientRequest;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.modules.patient.dto.UpdatePatientRequest;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    PatientResponse createPatient(CreatePatientRequest request);

    PatientDetailsResponse getMyProfile(UUID userId);
    PatientDetailsResponse updateMyProfile(UUID userId, UpdatePatientRequest request);
}
