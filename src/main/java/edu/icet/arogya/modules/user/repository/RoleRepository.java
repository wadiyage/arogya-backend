package edu.icet.arogya.modules.user.repository;

import edu.icet.arogya.modules.user.entity.Role;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<@NonNull Role, @NonNull Long> {
     Optional<Role> findByName(RoleName name);
}
