package edu.icet.arogya.modules.appointment.audit.service.impl;

import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogRequest;
import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogResponse;
import edu.icet.arogya.modules.appointment.audit.entity.AppointmentAuditLog;
import edu.icet.arogya.modules.appointment.audit.mapper.AppointmentAuditLogMapper;
import edu.icet.arogya.modules.appointment.audit.repository.AppointmentAuditLogRepository;
import edu.icet.arogya.modules.appointment.audit.service.AppointmentAuditService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AppointmentAuditServiceImpl implements AppointmentAuditService {

    private final AppointmentAuditLogRepository auditLogRepository;

    private final AppointmentAuditLogMapper auditLogMapper;

    @Override
    public void logStatusChange(AppointmentAuditLogRequest request) {
        AppointmentAuditLog auditLog = AppointmentAuditLog.builder()
                .appointment(request.getAppointment())
                .oldStatus(request.getOldStatus())
                .newStatus(request.getNewStatus())
                .actionType(request.getActionType())
                .reason(request.getReason())
                .changedByUserId(request.getUserId())
                .changedByRole(request.getUserRole())
                .metadata(request.getMetadata())
                .build();

        auditLogRepository.save(auditLog);
    }

    @Override
    public Page<@NonNull AppointmentAuditLogResponse> getLogsByAppointment(UUID appointmentId, Pageable pageable) {
        Page<@NonNull AppointmentAuditLog> page = auditLogRepository.findByAppointmentId(appointmentId, pageable);
        return page.map(auditLogMapper::mapToResponse);
    }
}
