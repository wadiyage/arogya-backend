package edu.icet.arogya.modules.medicalrecord.mapper;

import edu.icet.arogya.modules.medicalrecord.dto.MedicalRecordResponse;
import edu.icet.arogya.modules.medicalrecord.entity.MedicalRecord;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {
    public MedicalRecordResponse mapToResponse(MedicalRecord medicalRecord) {
        return MedicalRecordResponse.builder()
                .id(medicalRecord.getId())
                .patientId(medicalRecord.getPatient().getId())
                .patientName(medicalRecord.getPatient().getFullName())
                .doctorId(medicalRecord.getDoctor().getId())
                .doctorName(medicalRecord.getDoctor().getFullName())
                .symptoms(medicalRecord.getSymptoms())
                .notes(medicalRecord.getNotes())
                .visitedAt(medicalRecord.getVisitedAt())
                .createdAt(medicalRecord.getCreatedAt())
                .build();
    }
}
