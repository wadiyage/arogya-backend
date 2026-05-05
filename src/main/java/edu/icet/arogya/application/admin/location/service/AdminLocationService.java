package edu.icet.arogya.application.admin.location.service;

import edu.icet.arogya.application.admin.location.dto.CreateLocationRequest;
import edu.icet.arogya.application.admin.location.dto.LocationResponse;
import edu.icet.arogya.application.admin.location.dto.UpdateLocationRequest;
import edu.icet.arogya.security.user.UserPrincipal;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminLocationService {
    LocationResponse create(
            UserPrincipal user,
            CreateLocationRequest request
    );

    LocationResponse update(
            UserPrincipal user,
            UUID id,
            UpdateLocationRequest request
    );

    LocationResponse getById(UUID id);
    Page<@NonNull LocationResponse> getAll(Pageable pageable);

    Page<@NonNull LocationResponse> getActiveLocations(Pageable pageable);

    void activate(UserPrincipal user, UUID id);
    void deactivate(UserPrincipal user, UUID id);
}
