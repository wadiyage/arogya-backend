package edu.icet.arogya.modules.auth.service;

import edu.icet.arogya.modules.auth.dto.AuthResponse;
import edu.icet.arogya.modules.auth.dto.LoginRequest;
import edu.icet.arogya.modules.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
