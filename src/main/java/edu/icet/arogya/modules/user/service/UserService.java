package edu.icet.arogya.modules.user.service;

import edu.icet.arogya.modules.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
}
