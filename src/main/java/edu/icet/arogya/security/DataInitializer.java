package edu.icet.arogya.security;

import edu.icet.arogya.modules.user.entity.Role;
import edu.icet.arogya.modules.user.entity.RoleName;
import edu.icet.arogya.modules.user.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        for (RoleName roleName: RoleName.values()) {
            if(roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
}
