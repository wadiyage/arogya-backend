package edu.icet.arogya.modules.patient.service;

import edu.icet.arogya.modules.patient.dto.CreatePatientRequest;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    PatientResponse createPatient(CreatePatientRequest request);
    PatientResponse getPatientById(UUID id);
    PatientDetailsResponse getPatientDetails(UUID id);
    List<PatientResponse> getAllPatients();
}
