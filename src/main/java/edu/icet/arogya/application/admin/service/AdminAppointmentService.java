package edu.icet.arogya.application.admin.service;

import edu.icet.arogya.modules.appointment.dto.filter.AppointmentFilterRequest;
import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.user.entity.enums.RoleName;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AdminAppointmentService {
    Page<@NonNull AppointmentResponse> getAllAppointments(
            AppointmentFilterRequest filter,
            Pageable pageable
    );
    AppointmentResponse overrideStatus(UUID appointmentId, AppointmentStatus status, String reason, UUID adminId, RoleName role);
    void cancelAppointmentByAdmin(UUID appointmentId, UUID adminId, RoleName role);

    void bulkCancelAppointmentsByAdmin(List<UUID> appointmentIds, String reason, UUID adminId, RoleName role);
}
