package edu.icet.arogya.modules.location.service;

import edu.icet.arogya.application.admin.location.dto.CreateLocationRequest;
import edu.icet.arogya.application.admin.location.dto.LocationResponse;
import edu.icet.arogya.application.admin.location.dto.UpdateLocationRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LocationService {
    LocationResponse create(
            String name,
            String address,
            String city,
            String district,
            String contactNumber
    );

    LocationResponse update(
            UUID id,
            String name,
            String address,
            String city,
            String district,
            String contactNumber,
            Boolean active
    );

    LocationResponse getById(UUID id);
    Page<@NonNull LocationResponse> getAll(Pageable pageable);

    Page<@NonNull LocationResponse> getActiveLocations(Pageable pageable);

    void activate(UUID id);
    void deactivate(UUID id);
}
