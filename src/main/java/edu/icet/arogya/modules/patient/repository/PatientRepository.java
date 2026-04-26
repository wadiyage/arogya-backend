package edu.icet.arogya.modules.patient.repository;

import edu.icet.arogya.modules.patient.entity.Patient;
import edu.icet.arogya.modules.user.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<@NonNull Patient, @NonNull UUID> {
    Optional<Patient> findByUserId(UUID userId);
    Optional<Patient> findByUserEmail(String email);

    Optional<Patient> findByUser(User user);

    boolean existsByUser(User user);
}
