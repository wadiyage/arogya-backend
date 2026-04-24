package edu.icet.arogya.application.admin.service.impl;

import edu.icet.arogya.application.admin.dto.doctor.AdminUpdateDoctorRequest;
import edu.icet.arogya.application.admin.dto.doctor.CreateDoctorRequest;
import edu.icet.arogya.application.admin.dto.doctor.DoctorResponse;
import edu.icet.arogya.application.admin.service.AdminDoctorService;
import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.doctor.dto.DoctorDetailsResponse;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.doctor.mapper.DoctorMapper;
import edu.icet.arogya.modules.doctor.repository.DoctorRepository;
import edu.icet.arogya.modules.user.entity.Role;
import edu.icet.arogya.modules.user.entity.User;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import edu.icet.arogya.modules.user.repository.RoleRepository;
import edu.icet.arogya.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminDoctorServiceImpl implements AdminDoctorService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final DoctorMapper doctorMapper;

    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already in use: " + request.getEmail());
        }

        Role role = roleRepository.findByName(RoleName.DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: DOCTOR"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .user(user)
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .qualification(request.getQualification())
                .phoneNumber(request.getPhoneNumber())
                .hospitalName(request.getHospitalName())
                .consultationFee(request.getConsultationFee())
                .build();

        doctorRepository.save(doctor);

        return doctorMapper.mapToResponse(doctor);
    }

    @Override
    public DoctorDetailsResponse updateDoctor(UUID id, AdminUpdateDoctorRequest request) {
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
        return doctorMapper.mapToDetailsResponse(doctor);
    }

    @Override
    public void deactivateDoctor(UUID id, boolean available) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        doctor.setAvailable(available);
        doctorRepository.save(doctor);
    }

    @Override
    public DoctorDetailsResponse getDoctorDetails(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return doctorMapper.mapToDetailsResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctorMapper::mapToResponse)
                .toList();
    }
}
