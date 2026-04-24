package edu.icet.arogya.modules.patient.service.impl;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.common.exception.UnauthorizedException;
import edu.icet.arogya.modules.patient.dto.CreatePatientRequest;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.modules.patient.dto.UpdatePatientRequest;
import edu.icet.arogya.modules.patient.entity.Patient;
import edu.icet.arogya.modules.patient.mapper.PatientMapper;
import edu.icet.arogya.modules.patient.repository.PatientRepository;
import edu.icet.arogya.modules.patient.service.PatientService;
import edu.icet.arogya.modules.user.entity.User;
import edu.icet.arogya.modules.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    @Override
    public PatientResponse createPatient(CreatePatientRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        if(!user.getRole().getName().equals("PATIENT")) {
            throw new UnauthorizedException("User with ID: " + request.getUserId() + " is not a patient");
        }

        if(patientRepository.existsByUser(user)) {
            throw new BadRequestException("Patient already exists for user with ID: " + request.getUserId());
        }

        Patient patient = Patient.builder()
                .user(user)
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactNumber(request.getEmergencyContactNumber())
                .build();

        patientRepository.save(patient);

        return patientMapper.mapToResponse(patient);
    }

    @Override
    public PatientDetailsResponse getMyProfile(String email) {
        Patient patient = patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found for email: " + email));
        return patientMapper.mapToDetailsResponse(patient);
    }

    @Override
    public PatientDetailsResponse updateMyProfile(String email, UpdatePatientRequest request) {
        Patient patient = patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found for email: " + email));

        if(request.getFullName() != null) patient.setFullName(request.getFullName());
        if(request.getDateOfBirth() != null) patient.setDateOfBirth(request.getDateOfBirth());
        if(request.getGender() != null) patient.setGender(request.getGender());
        if(request.getBloodGroup() != null) patient.setBloodGroup(request.getBloodGroup());
        if(request.getPhoneNumber() != null) patient.setPhoneNumber(request.getPhoneNumber());
        if(request.getAddress() != null) patient.setAddress(request.getAddress());
        if(request.getEmergencyContactName() != null) patient.setEmergencyContactName(request.getEmergencyContactName());
        if(request.getEmergencyContactNumber() != null) patient.setEmergencyContactNumber(request.getEmergencyContactNumber());

        patientRepository.save(patient);
        return patientMapper.mapToDetailsResponse(patient);
    }

    @Override
    public PatientResponse getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
        return patientMapper.mapToResponse(patient);
    }

    @Override
    public PatientDetailsResponse getPatientDetails(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
        return patientMapper.mapToDetailsResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::mapToResponse)
                .toList();
    }
}
