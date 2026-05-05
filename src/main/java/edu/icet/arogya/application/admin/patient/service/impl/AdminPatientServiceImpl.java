package edu.icet.arogya.application.admin.patient.service.impl;

import edu.icet.arogya.application.admin.audit.dto.CreateAuditLogRequest;
import edu.icet.arogya.application.admin.audit.service.AdminAuditService;
import edu.icet.arogya.application.admin.patient.dto.AdminUpdatePatientRequest;
import edu.icet.arogya.application.admin.patient.service.AdminPatientService;
import edu.icet.arogya.common.exception.UnauthorizedException;
import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.patient.dto.PatientDetailsResponse;
import edu.icet.arogya.modules.patient.dto.PatientResponse;
import edu.icet.arogya.modules.patient.entity.Patient;
import edu.icet.arogya.modules.patient.mapper.PatientMapper;
import edu.icet.arogya.modules.patient.repository.PatientRepository;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import edu.icet.arogya.security.user.UserPrincipal;
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

    private final AdminAuditService auditService;

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
    public PatientDetailsResponse updatePatient(UserPrincipal user, UUID id, AdminUpdatePatientRequest request) {
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

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.UPDATE)
                        .entityType(AuditEntityType.PATIENT)
                        .entityId(patient.getId())
                        .description("Updated patient with name " + patient.getFullName())
                        .metadata("active=" + patient.isActive())
                        .build()
        );

        return patientMapper.mapToDetailsResponse(patient);
    }

    @Override
    public void deactivatePatient(UserPrincipal user, UUID id) {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));
            patient.setActive(false);

            auditService.log(
                    CreateAuditLogRequest.builder()
                            .userId(user.getId())
                            .userRole(RoleName.valueOf(user.getRole()))
                            .action(AuditAction.UPDATE)
                            .entityType(AuditEntityType.PATIENT)
                            .entityId(patient.getId())
                            .description("Deactivated patient with name " + patient.getFullName())
                            .metadata("active=" + patient.isActive())
                            .build()
            );

            patientRepository.save(patient);
    }

    @Override
    public void activatePatient(UserPrincipal user, UUID id) {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new UnauthorizedException("Patient not found with ID: " + id));
            patient.setActive(true);

            auditService.log(
                    CreateAuditLogRequest.builder()
                            .userId(user.getId())
                            .userRole(RoleName.valueOf(user.getRole()))
                            .action(AuditAction.UPDATE)
                            .entityType(AuditEntityType.PATIENT)
                            .entityId(patient.getId())
                            .description("Activated patient with name " + patient.getFullName())
                            .metadata("active=" + patient.isActive())
                            .build()
            );

            patientRepository.save(patient);
    }
}
