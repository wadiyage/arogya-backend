package edu.icet.arogya.modules.appointment.audit.repository;

import edu.icet.arogya.modules.appointment.audit.entity.AppointmentAuditLog;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentAuditLogRepository extends JpaRepository<@NonNull AppointmentAuditLog, @NonNull UUID> {
    List<AppointmentAuditLog> findByAppointmentId(UUID appointmentId);
}
