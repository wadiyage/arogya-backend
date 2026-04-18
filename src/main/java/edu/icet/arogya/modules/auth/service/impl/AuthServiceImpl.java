package edu.icet.arogya.modules.auth.service.impl;

import edu.icet.arogya.modules.auth.dto.AuthResponse;
import edu.icet.arogya.modules.auth.dto.LoginRequest;
import edu.icet.arogya.modules.auth.dto.RegisterRequest;
import edu.icet.arogya.modules.auth.service.AuthService;
import edu.icet.arogya.modules.user.entity.Role;
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

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRoleName()));

        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName().name());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName().name());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .userId(user.getId())
                .build();


    }
}
