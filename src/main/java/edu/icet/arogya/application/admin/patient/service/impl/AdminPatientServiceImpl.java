package edu.icet.arogya.application.admin.patient.service.impl;

import edu.icet.arogya.application.admin.patient.dto.AdminUpdatePatientRequest;
import edu.icet.arogya.application.admin.patient.service.AdminPatientService;
import edu.icet.arogya.common.exception.UnauthorizedException;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.modules.patient.entity.Patient;
import edu.icet.arogya.modules.patient.mapper.PatientMapper;
import edu.icet.arogya.modules.patient.repository.PatientRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPatientServiceImpl implements AdminPatientService {

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    @Override
    public PatientDetailsResponse getPatientDetails(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));

        return patientMapper.mapToDetailsResponse(patient);
    }

    @Override
    public Page<@NonNull PatientResponse> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(patientMapper::mapToResponse);
    }

    @Override
    public PatientDetailsResponse updatePatient(UUID id, AdminUpdatePatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));

        if(request.getFullName() != null) {
            patient.setFullName(request.getFullName());
        }

        if(request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }

        if(request.getGender() != null) {
            patient.setGender(request.getGender());
        }

        if(request.getBloodGroup() != null) {
            patient.setBloodGroup(request.getBloodGroup());
        }

        if(request.getPhoneNumber() != null) {
            patient.setPhoneNumber(request.getPhoneNumber());
        }

        if(request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }

        if(request.getEmergencyContactName() != null) {
            patient.setEmergencyContactName(request.getEmergencyContactName());
        }

        if(request.getEmergencyContactNumber() != null) {
            patient.setEmergencyContactNumber(request.getEmergencyContactNumber());
        }


        if(request.getActive() != null) {
            patient.setActive(request.getActive());
        }

        patientRepository.save(patient);
        return patientMapper.mapToDetailsResponse(patient);
    }

    @Override
    public void deactivatePatient(UUID id) {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));
            patient.setActive(false);
            patientRepository.save(patient);
    }

    @Override
    public void activatePatient(UUID id) {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));
            patient.setActive(true);
            patientRepository.save(patient);
    }
}
