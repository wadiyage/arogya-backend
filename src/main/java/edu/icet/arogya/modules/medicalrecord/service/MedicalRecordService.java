package edu.icet.arogya.modules.medicalrecord.service;

import edu.icet.arogya.modules.medicalrecord.dto.CreateMedicalRecordRequest;
import edu.icet.arogya.modules.medicalrecord.dto.MedicalRecordResponse;
import org.springframework.security.core.Authentication;

public interface MedicalRecordService {
    MedicalRecordResponse createMedicalRecord(
            CreateMedicalRecordRequest request,
            Authentication authentication
    );
}
