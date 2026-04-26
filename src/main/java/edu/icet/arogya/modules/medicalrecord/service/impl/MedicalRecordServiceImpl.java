package edu.icet.arogya.modules.medicalrecord.service.impl;

import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.doctor.repository.DoctorRepository;
import edu.icet.arogya.modules.medicalrecord.dto.CreateMedicalRecordRequest;
import edu.icet.arogya.modules.medicalrecord.dto.MedicalRecordResponse;
import edu.icet.arogya.modules.medicalrecord.entity.MedicalRecord;
import edu.icet.arogya.modules.medicalrecord.mapper.MedicalRecordMapper;
import edu.icet.arogya.modules.medicalrecord.repository.MedicalRecordRepository;
import edu.icet.arogya.modules.medicalrecord.service.MedicalRecordService;
import edu.icet.arogya.modules.patient.entity.Patient;
import edu.icet.arogya.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public MedicalRecordResponse createMedicalRecord(CreateMedicalRecordRequest request, Authentication authentication) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + request.getPatientId()
                ));

        String email = authentication.getName();
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with email: " + email
                ));

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .symptoms(request.getSymptoms())
                .notes(request.getNotes())
                .visitedAt(LocalDateTime.now())
                .build();

        medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.mapToResponse(medicalRecord);
    }
}
