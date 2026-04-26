package edu.icet.arogya.application.admin.service;

import edu.icet.arogya.application.admin.dto.patient.AdminUpdatePatientRequest;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminPatientService {
    PatientDetailsResponse getPatientDetails(UUID id);
    Page<@NonNull PatientResponse> getAllPatients(Pageable pageable);

    PatientDetailsResponse updatePatient(UUID id, AdminUpdatePatientRequest request);

    void deactivatePatient(UUID id);
    void activatePatient(UUID id);
}
