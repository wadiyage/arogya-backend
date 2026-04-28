package edu.icet.arogya.modules.appointment.audit.service;

import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogRequest;
import edu.icet.arogya.modules.appointment.audit.dto.AppointmentAuditLogResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AppointmentAuditService {
    void logStatusChange(AppointmentAuditLogRequest request);

    Page<@NonNull AppointmentAuditLogResponse> getLogsByAppointment(UUID appointmentId, Pageable pageable);
}
