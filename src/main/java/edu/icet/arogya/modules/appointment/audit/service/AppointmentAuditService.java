package edu.icet.arogya.modules.appointment.audit.service;

import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;

public interface AppointmentAuditService {
    void logStatusChange(
            Appointment appointment,
            AppointmentStatus oldStatus,
            AppointmentStatus newStatus,
            String reason,
            String changedBy
    );
}
