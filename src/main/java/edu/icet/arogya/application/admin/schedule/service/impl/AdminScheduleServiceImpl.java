package edu.icet.arogya.application.admin.schedule.service.impl;

import edu.icet.arogya.application.admin.audit.dto.CreateAuditLogRequest;
import edu.icet.arogya.application.admin.audit.service.AdminAuditService;
import edu.icet.arogya.application.admin.schedule.dto.CreateScheduleRequest;
import edu.icet.arogya.application.admin.schedule.dto.ScheduleResponse;
import edu.icet.arogya.application.admin.schedule.dto.UpdateScheduleRequest;
import edu.icet.arogya.application.admin.schedule.service.AdminScheduleService;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.appointment.schedule.mapper.ScheduleMapper;
import edu.icet.arogya.modules.appointment.schedule.service.DoctorScheduleService;
import edu.icet.arogya.modules.audit.entity.enums.AuditAction;
import edu.icet.arogya.modules.audit.entity.enums.AuditEntityType;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import edu.icet.arogya.security.user.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminScheduleServiceImpl implements AdminScheduleService {

    private final DoctorScheduleService doctorScheduleService;

    private final ScheduleMapper scheduleMapper;

    private final AdminAuditService auditService;

    @Override
    public ScheduleResponse create(
            UserPrincipal user,
            CreateScheduleRequest request
    ) {
        DoctorSchedule newSchedule = doctorScheduleService.create(
                request.getDoctorId(),
                request.getLocationId(),
                request.getDate(),
                request.getMaxTokens(),
                request.getSessionName(),
                request.getConsultationFee()
        );

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.CREATE)
                        .entityType(AuditEntityType.DOCTOR_SCHEDULE)
                        .entityId(newSchedule.getId())
                        .description("Created schedule for doctor " + newSchedule.getDoctor().getFullName() + " at location " + newSchedule.getLocation().getName() + " on date " + newSchedule.getScheduleDate())
                        .metadata("doctorId=" + newSchedule.getDoctor().getId() + ",locationId=" + newSchedule.getLocation().getId() + ",date=" + newSchedule.getScheduleDate())
                        .build()
        );

        return scheduleMapper.mapToResponse(newSchedule);
    }

    @Override
    public ScheduleResponse update(
            UserPrincipal user,
            UUID scheduleId,
            UpdateScheduleRequest request
    ) {
        DoctorSchedule exiting = doctorScheduleService.update(
                scheduleId,
                request.getDate(),
                request.getMaxTokens(),
                request.getSessionName(),
                request.getConsultationFee()
        );

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.UPDATE)
                        .entityType(AuditEntityType.DOCTOR_SCHEDULE)
                        .entityId(exiting.getId())
                        .description("Updated schedule for doctor " + exiting.getDoctor().getFullName() + " at location " + exiting.getLocation().getName() + " on date " + exiting.getScheduleDate())
                        .metadata("doctorId=" + exiting.getDoctor().getId() + ",locationId=" + exiting.getLocation().getId() + ",date=" + exiting.getScheduleDate())
                        .build()
        );

        return scheduleMapper.mapToResponse(exiting);
    }

    @Override
    public ScheduleResponse getById(UUID scheduleId) {
        DoctorSchedule schedule = doctorScheduleService.getById(scheduleId);
        return scheduleMapper.mapToResponse(schedule);
    }

    @Override
    public Page<@NonNull ScheduleResponse> getByDoctor(
            UUID doctorId,
            LocalDate date,
            Pageable pageable
    ) {
        return doctorScheduleService.getByDoctor(doctorId, date, pageable)
                .map(scheduleMapper::mapToResponse);
    }

    @Override
    public Page<@NonNull ScheduleResponse> getByLocation(
            UUID locationId,
            LocalDate date,
            Pageable pageable
    ) {
        return doctorScheduleService.getByLocation(locationId, date, pageable)
                .map(scheduleMapper::mapToResponse);
    }

    @Override
    public void activate(
            UserPrincipal user,
            UUID scheduleId
    ) {
        doctorScheduleService.activate(scheduleId);

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.ACTIVATE)
                        .entityType(AuditEntityType.DOCTOR_SCHEDULE)
                        .entityId(scheduleId)
                        .description("Activated schedule with ID " + scheduleId)
                        .metadata("scheduleId=" + scheduleId)
                        .build()
        );
    }

    @Override
    public void deactivate(
            UserPrincipal user,
            UUID scheduleId
    ) {
        doctorScheduleService.deactivate(scheduleId);

        auditService.log(
                CreateAuditLogRequest.builder()
                        .userId(user.getId())
                        .userRole(RoleName.valueOf(user.getRole()))
                        .action(AuditAction.DEACTIVATE)
                        .entityType(AuditEntityType.DOCTOR_SCHEDULE)
                        .entityId(scheduleId)
                        .description("Deactivated schedule with ID " + scheduleId)
                        .metadata("scheduleId=" + scheduleId)
                        .build()
        );
    }
}
