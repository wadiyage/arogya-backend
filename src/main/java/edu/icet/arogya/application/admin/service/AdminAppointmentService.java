package edu.icet.arogya.application.admin.service;

import edu.icet.arogya.modules.appointment.dto.filter.AppointmentFilterRequest;
import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminAppointmentService {
    Page<@NonNull AppointmentResponse> getAllAppointments(
            AppointmentFilterRequest filter,
            Pageable pageable
    );
    AppointmentResponse overrideStatus(UUID appointmentId, AppointmentStatus status, String reason);
    void cancelAppointmentByAdmin(UUID appointmentId);
}
