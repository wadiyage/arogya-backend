package edu.icet.arogya.modules.auth.service.impl;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.common.exception.UnauthorizedException;
import edu.icet.arogya.modules.auth.dto.AuthResponse;
import edu.icet.arogya.modules.auth.dto.LoginRequest;
import edu.icet.arogya.modules.auth.dto.RegisterRequest;
import edu.icet.arogya.modules.auth.service.AuthService;
import edu.icet.arogya.modules.doctor.service.DoctorService;
import edu.icet.arogya.modules.patient.dto.CreatePatientRequest;
import edu.icet.arogya.modules.patient.service.PatientService;
import edu.icet.arogya.modules.user.entity.Role;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import edu.icet.arogya.modules.user.entity.User;
import edu.icet.arogya.modules.user.repository.RoleRepository;
import edu.icet.arogya.modules.user.repository.UserRepository;
import edu.icet.arogya.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PatientService patientService;
    private final DoctorService doctorService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }

        if(!request.getRoleName().name().equals("PATIENT")) {
            throw new UnauthorizedException("Only patients can self-register. Please contact admin to create an account with role: " + request.getRoleName());
        }

        Role role = roleRepository.findByName(RoleName.PATIENT)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: PATIENT"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        patientService.createPatient(
                CreatePatientRequest.builder()
                        .userId(user.getId())
                        .build()
        );

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                role.getName().name()
        );

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .role(role.getName().name())
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().getName().name()
        );

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .userId(user.getId())
                .build();


    }
}
