package edu.icet.arogya.modules.location.repository;

import edu.icet.arogya.modules.location.entity.Location;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<@NonNull Location, @NonNull UUID> {
    Page<@NonNull Location> findByActiveTrue(Pageable pageable);

    List<@NonNull Location> findByActiveTrueAndCity(String city);
    boolean existsByNameIgnoreCaseAndCityIgnoreCase(String name, String city);
}
