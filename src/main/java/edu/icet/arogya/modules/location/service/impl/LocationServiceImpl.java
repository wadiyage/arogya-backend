package edu.icet.arogya.modules.location.service.impl;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.application.admin.location.dto.CreateLocationRequest;
import edu.icet.arogya.application.admin.location.dto.LocationResponse;
import edu.icet.arogya.application.admin.location.dto.UpdateLocationRequest;
import edu.icet.arogya.modules.location.entity.Location;
import edu.icet.arogya.modules.location.mapper.LocationMapper;
import edu.icet.arogya.modules.location.repository.LocationRepository;
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
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    @Override
    public LocationResponse create(
            String name,
            String address,
            String city,
            String district,
            String contactNumber
    ) {
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Location name is required");
        }

        if (city == null || city.isEmpty()) {
            throw new BadRequestException("City is required");
        }

        boolean exists = locationRepository.existsByNameIgnoreCaseAndCityIgnoreCase(
                name,
                city
        );
        if (exists) {
            throw new BadRequestException("Location with name " + name + " and city " + city + " already exists");
        }

        Location newLocation = Location.builder()
                .name(name)
                .address(address)
                .city(city)
                .district(district)
                .contactNumber(contactNumber)
                .build();

        Location saved = locationRepository.save(newLocation);

        return locationMapper.mapToResponse(saved);
    }

    @Override
    public LocationResponse update(
            UUID id,
            String name,
            String address,
            String city,
            String district,
            String contactNumber,
            Boolean active
    ) {
        Location currentLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));

        if (name != null) {
            currentLocation.setName(name);
        }
        if (address != null) {
            currentLocation.setAddress(address);
        }
        if (city != null) {
            currentLocation.setCity(city);
        }
        if (district != null) {
            currentLocation.setDistrict(district);
        }
        if (contactNumber != null) {
            currentLocation.setContactNumber(contactNumber);
        }
        if (active != null) {
            currentLocation.setActive(active);
        }

        Location updated = locationRepository.save(currentLocation);

        return locationMapper.mapToResponse(updated);
    }

    @Override
    public LocationResponse getById(UUID id) {
        Location exitingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        return locationMapper.mapToResponse(exitingLocation);
    }

    @Override
    public Page<@NonNull LocationResponse> getAll(Pageable pageable) {
        Page<@NonNull Location> exitingLocations = locationRepository.findAll(pageable);
        return exitingLocations.map(locationMapper::mapToResponse);
    }

    @Override
    public Page<@NonNull LocationResponse> getActiveLocations(Pageable pageable) {
        Page<@NonNull Location> activeLocations = locationRepository.findByActiveTrue(pageable);
        return activeLocations.map(locationMapper::mapToResponse);
    }

    @Override
    public void activate(UUID id) {
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
            if (!location.isActive()) {
                location.setActive(true);
            }
    }

    @Override
    public void deactivate(UUID id) {
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
            if (location.isActive()) {
                location.setActive(false);
            }
    }
}
