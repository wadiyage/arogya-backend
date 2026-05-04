package edu.icet.arogya.modules.location.mapper;

import edu.icet.arogya.application.admin.location.dto.LocationResponse;
import edu.icet.arogya.modules.location.entity.Location;

public class LocationMapper {
    public LocationResponse mapToResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .city(location.getCity())
                .district(location.getDistrict())
                .contactNumber(location.getContactNumber())
                .active(location.isActive())
                .createdAt(location.getCreatedAt())
                .updatedAt(location.getUpdatedAt())
                .build();
    }
}
