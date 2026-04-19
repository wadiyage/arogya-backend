package edu.icet.arogya.modules.auth.dto;

import edu.icet.arogya.modules.user.entity.RoleName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String email;
    private String password;
    private RoleName roleName;
}
