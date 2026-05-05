package edu.icet.arogya.application.admin.patient.service;

import edu.icet.arogya.application.admin.patient.dto.AdminUpdatePatientRequest;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.security.user.UserPrincipal;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminPatientService {
    PatientDetailsResponse getPatientDetails(UUID id);
    Page<@NonNull PatientResponse> getAllPatients(Pageable pageable);

    PatientDetailsResponse updatePatient(UserPrincipal user, UUID id, AdminUpdatePatientRequest request);

    void deactivatePatient(UserPrincipal user, UUID id);
    void activatePatient(UserPrincipal user, UUID id);
}
