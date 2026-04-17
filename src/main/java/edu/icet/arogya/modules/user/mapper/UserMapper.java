package edu.icet.arogya.modules.user.mapper;

import edu.icet.arogya.modules.user.dto.UserResponse;
import edu.icet.arogya.modules.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
