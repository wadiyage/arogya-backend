package edu.icet.arogya.modules.appointment.audit.mapper;

import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogResponse;
import edu.icet.arogya.modules.appointment.audit.entity.AppointmentAuditLog;

public class AppointmentAuditLogMapper {
    public AppointmentAuditLogResponse mapToResponse(AppointmentAuditLog log) {
        return AppointmentAuditLogResponse.builder()
                .id(log.getId())
                .appointmentId(log.getAppointment().getId())
                .actionType(log.getActionType())
                .oldStatus(log.getOldStatus())
                .newStatus(log.getNewStatus())
                .reason(log.getReason())
                .changedByUserId(log.getChangedByUserId())
                .changedByRole(log.getChangedByRole())
                .metadata(log.getMetadata())
                .changedAt(log.getChangedAt())
                .build();
    }
}
