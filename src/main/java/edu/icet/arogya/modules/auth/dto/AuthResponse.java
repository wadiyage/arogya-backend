package edu.icet.arogya.modules.auth.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String type; // Bearer

    private String email;
    private String role;

    private UUID userId;
}
