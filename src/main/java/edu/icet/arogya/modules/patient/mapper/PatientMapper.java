package edu.icet.arogya.modules.patient.mapper;

import edu.icet.arogya.modules.medicalrecord.dto.MedicalRecordSummary;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.modules.patient.entity.Patient;

import java.util.List;
import java.util.UUID;

public class PatientMapper {
    public PatientResponse mapToResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .userId(patient.getUser().getId())
                .email(patient.getUser().getEmail())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .isActive(patient.isActive())
                .createdAt(patient.getCreatedAt())
                .build();
    }

    public PatientDetailsResponse mapToDetailsResponse(Patient patient) {
        List<MedicalRecordSummary> records = patient.getMedicalRecords() == null
                ? List.of()
                : patient.getMedicalRecords().stream()
                        .map(record -> {
                            var doctor = record.getDoctor();
                            UUID doctorId = doctor != null ? record.getDoctor().getId() : null;
                            String doctorName = doctor != null ? record.getDoctor().getFullName() : "N/A";
                            return MedicalRecordSummary.builder()
                                    .id(record.getId())
                                    .doctorId(doctorId)
                                    .doctorName(doctorName)
                                    .symptoms(record.getSymptoms())
                                    .notes(record.getNotes())
                                    .visitedAt(record.getVisitedAt())
                                    .build();
                        })
                        .toList();

        return PatientDetailsResponse.builder()
                .id(patient.getId())
                .userId(patient.getUser().getId())
                .email(patient.getUser().getEmail())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .isActive(patient.isActive())
                .createdAt(patient.getCreatedAt())
                .medicalRecords(records)
                .build();
    }
}
