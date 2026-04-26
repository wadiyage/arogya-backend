package edu.icet.arogya.modules.doctor.repository;

import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.user.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<@NonNull Doctor, @NonNull UUID> {
    Optional<Doctor> findByUserId(UUID userId);
    Optional<Doctor> findByUserEmail(String email);

    Optional<Doctor> findByUser(User user);
    boolean existsByUser(User user);
}
