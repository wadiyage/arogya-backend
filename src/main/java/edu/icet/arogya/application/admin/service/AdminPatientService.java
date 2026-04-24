package edu.icet.arogya.application.admin.service;

import edu.icet.arogya.application.admin.dto.patient.AdminUpdatePatientRequest;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;

import java.util.List;
import java.util.UUID;

public interface AdminPatientService {
    PatientDetailsResponse getPatientDetails(UUID id);
    List<PatientResponse> getAllPatients();

    PatientDetailsResponse updatePatient(UUID id, AdminUpdatePatientRequest request);

    void deactivatePatient(UUID id);
    void activatePatient(UUID id);
}
