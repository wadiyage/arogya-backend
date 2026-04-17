package edu.icet.arogya.modules.user.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String role;
    private boolean isActive;
    private LocalDateTime createdAt;
}
