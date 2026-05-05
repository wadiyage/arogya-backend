package edu.icet.arogya.application.admin.doctor.service.impl;

import edu.icet.arogya.application.admin.audit.dto.CreateAuditLogRequest;
import edu.icet.arogya.application.admin.audit.service.AdminAuditService;
import edu.icet.arogya.application.admin.doctor.dto.AdminUpdateDoctorRequest;
import edu.icet.arogya.application.admin.doctor.dto.CreateDoctorRequest;
import edu.icet.arogya.application.admin.doctor.dto.DoctorResponse;
import edu.icet.arogya.application.admin.doctor.service.AdminDoctorService;
import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.doctor.dto.DoctorDetailsResponse;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.doctor.mapper.DoctorMapper;
import edu.icet.arogya.modules.doctor.repository.DoctorRepository;
import edu.icet.arogya.modules.user.entity.Role;
import edu.icet.arogya.modules.user.entity.User;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import edu.icet.arogya.modules.user.repository.RoleRepository;
import edu.icet.arogya.modules.user.repository.UserRepository;
import edu.icet.arogya.security.user.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminDoctorServiceImpl implements AdminDoctorService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final DoctorMapper doctorMapper;

    private final AdminAuditService auditService;

    @Override
    public DoctorResponse createDoctor(UserPrincipal user, CreateDoctorRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already in use: " + request.getEmail());
        }

        Role role = roleRepository.findByName(RoleName.DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: DOCTOR"));

        User exitingUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(exitingUser);

        Doctor doctor = Doctor.builder()
                .user(exitingUser)
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .qualification(request.getQualification())
                .phoneNumber(request.getPhoneNumber())
                .hospitalName(request.getHospitalName())
                .consultationFee(request.getConsultationFee())
                .build();

        doctorRepository.save(doctor);

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.CREATE)
                        .entityType(AuditEntityType.DOCTOR)
                        .entityId(doctor.getId())
                        .description("Created doctor with name " + doctor.getFullName())
                        .metadata("specialization=" + doctor.getSpecialization())
                        .build()
        );

        return doctorMapper.mapToResponse(doctor);
    }

    @Override
    public DoctorDetailsResponse updateDoctor(UserPrincipal user, UUID id, AdminUpdateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        if(request.getSpecialization() != null) {
            doctor.setSpecialization(request.getSpecialization());
        }

        if(request.getLicenseNumber() != null) {
            doctor.setLicenseNumber(request.getLicenseNumber());
        }

        if(request.getHospitalName() != null) {
            doctor.setHospitalName(request.getHospitalName());
        }

        if(request.getConsultationFee() != null) {
            doctor.setConsultationFee(request.getConsultationFee());
        }

        if(request.getAvailable() != null) {
            doctor.setAvailable(request.getAvailable());
        }

        doctorRepository.save(doctor);

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.UPDATE)
                        .entityType(AuditEntityType.DOCTOR)
                        .entityId(doctor.getId())
                        .description("Updated doctor with name " + doctor.getFullName())
                        .metadata("specialization=" + doctor.getSpecialization())
                        .build()
        );

        return doctorMapper.mapToDetailsResponse(doctor);
    }

    @Override
    public void deactivateDoctor(UserPrincipal user, UUID id, boolean available) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        doctor.setAvailable(available);

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.UPDATE)
                        .entityType(AuditEntityType.DOCTOR)
                        .entityId(doctor.getId())
                        .description((available ? "Activated" : "Deactivated") + " doctor with name " + doctor.getFullName())
                        .metadata("specialization=" + doctor.getSpecialization())
                        .build()
        );

        doctorRepository.save(doctor);
    }

    @Override
    public DoctorDetailsResponse getDoctorDetails(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return doctorMapper.mapToDetailsResponse(doctor);
    }

    @Override
    public Page<@NonNull DoctorResponse> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(doctorMapper::mapToResponse);
    }
}
