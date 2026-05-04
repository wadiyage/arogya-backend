package edu.icet.arogya.application.admin.location.service.impl;

import edu.icet.arogya.application.admin.location.dto.CreateLocationRequest;
import edu.icet.arogya.application.admin.location.dto.LocationResponse;
import edu.icet.arogya.application.admin.location.dto.UpdateLocationRequest;
import edu.icet.arogya.application.admin.location.service.AdminLocationService;
import edu.icet.arogya.modules.location.service.LocationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminLocationServiceImpl implements AdminLocationService {

    private final LocationService locationService;

    @Override
    public LocationResponse create(CreateLocationRequest request) {
        return locationService.create(
                request.getName(),
                request.getAddress(),
                request.getCity(),
                request.getDistrict(),
                request.getContactNumber()
        );
    }

    @Override
    public LocationResponse update(UUID id, UpdateLocationRequest request) {
        return locationService.update(
                id,
                request.getName(),
                request.getAddress(),
                request.getCity(),
                request.getDistrict(),
                request.getContactNumber(),
                request.getActive()
        );
    }

    @Override
    public LocationResponse getById(UUID id) {
        return locationService.getById(id);
    }

    @Override
    public Page<@NonNull LocationResponse> getAll(Pageable pageable) {
        return locationService.getAll(pageable);
    }

    @Override
    public Page<@NonNull LocationResponse> getActiveLocations(Pageable pageable) {
        return locationService.getActiveLocations(pageable);
    }

    @Override
    public void activate(UUID id) {
        locationService.activate(id);
    }

    @Override
    public void deactivate(UUID id) {
        locationService.deactivate(id);
    }
}
