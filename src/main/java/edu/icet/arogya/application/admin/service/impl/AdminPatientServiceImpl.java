package edu.icet.arogya.application.admin.service.impl;

import edu.icet.arogya.application.admin.dto.patient.AdminUpdatePatientRequest;
import edu.icet.arogya.application.admin.service.AdminPatientService;
import edu.icet.arogya.common.exception.UnauthorizedException;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.modules.patient.entity.Patient;
import edu.icet.arogya.modules.patient.mapper.PatientMapper;
import edu.icet.arogya.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminPatientServiceImpl implements AdminPatientService {

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    @Override
    public PatientDetailsResponse getPatientDetails(UUID id) {
        Patient patient = patientRepository.findByUserId(id)
                .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));

        return patientMapper.mapToDetailsResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::mapToResponse)
                .toList();
    }

    @Override
    public PatientDetailsResponse updatePatient(UUID id, AdminUpdatePatientRequest request) {
        Patient patient = patientRepository.findByUserId(id)
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
            Patient patient = patientRepository.findByUserId(id)
                    .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));
            patient.setActive(false);
            patientRepository.save(patient);
    }

    @Override
    public void activatePatient(UUID id) {
            Patient patient = patientRepository.findByUserId(id)
                    .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));
            patient.setActive(true);
            patientRepository.save(patient);
    }
}
