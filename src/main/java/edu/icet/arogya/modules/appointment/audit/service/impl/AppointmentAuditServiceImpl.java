package edu.icet.arogya.modules.appointment.audit.service.impl;

import edu.icet.arogya.modules.appointment.audit.entity.AppointmentAuditLog;
import edu.icet.arogya.modules.appointment.audit.repository.AppointmentAuditLogRepository;
import edu.icet.arogya.modules.appointment.audit.service.AppointmentAuditService;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppointmentAuditServiceImpl implements AppointmentAuditService {

    private final AppointmentAuditLogRepository auditLogRepository;

    @Override
    public void logStatusChange(
            Appointment appointment,
            AppointmentStatus oldStatus,
            AppointmentStatus newStatus,
            String reason,
            String changedBy
    ) {
        AppointmentAuditLog auditLog = AppointmentAuditLog.builder()
                .appointment(appointment)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .reason(reason)
                .changedBy(changedBy)
                .build();

        auditLogRepository.save(auditLog);
    }
}
