package edu.icet.arogya.modules.user.service;

import edu.icet.arogya.modules.user.dto.UserResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email);
    Page<@NonNull UserResponse> getAllUsers(Pageable pageable);
}
