package edu.icet.arogya.application.admin.location.service.impl;

import edu.icet.arogya.application.admin.audit.dto.CreateAuditLogRequest;
import edu.icet.arogya.application.admin.audit.service.AdminAuditService;
import edu.icet.arogya.application.admin.location.dto.CreateLocationRequest;
import edu.icet.arogya.application.admin.location.dto.LocationResponse;
import edu.icet.arogya.application.admin.location.dto.UpdateLocationRequest;
import edu.icet.arogya.application.admin.location.service.AdminLocationService;
import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.location.service.LocationService;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import edu.icet.arogya.security.user.UserPrincipal;
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

    private final AdminAuditService auditService;

    @Override
    public LocationResponse create(UserPrincipal user, CreateLocationRequest request) {
        LocationResponse response = locationService.create(
                request.getName(),
                request.getAddress(),
                request.getCity(),
                request.getDistrict(),
                request.getContactNumber()
        );

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.CREATE)
                        .entityType(AuditEntityType.LOCATION)
                        .entityId(response.getId())
                        .description("Created location with name " + response.getName())
                        .metadata("city=" + response.getCity())
                        .build()
        );
        return response;
    }

    @Override
    public LocationResponse update(
            UserPrincipal user,
            UUID id,
            UpdateLocationRequest request
    ) {
        LocationResponse response = locationService.update(
                id,
                request.getName(),
                request.getAddress(),
                request.getCity(),
                request.getDistrict(),
                request.getContactNumber(),
                request.getActive()
        );

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.UPDATE)
                        .entityType(AuditEntityType.LOCATION)
                        .entityId(response.getId())
                        .description("Updated location with name " + response.getName())
                        .metadata("city=" + response.getCity())
                        .build()
        );
        return response;
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
    public void activate(UserPrincipal user, UUID id) {
        locationService.activate(id);

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.ACTIVATE)
                        .entityType(AuditEntityType.LOCATION)
                        .entityId(id)
                        .description("Activated location with id " + id)
                        .build()
        );
    }

    @Override
    public void deactivate(UserPrincipal user, UUID id) {
        locationService.deactivate(id);

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.DEACTIVATE)
                        .entityType(AuditEntityType.LOCATION)
                        .entityId(id)
                        .description("Deactivated location with id " + id)
                        .build()
        );

    }
}
