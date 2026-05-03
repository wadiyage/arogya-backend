package edu.icet.arogya.modules.appointment.service;

import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;

import java.util.List;
import java.util.UUID;

public interface DoctorAppointmentService {
    List<AppointmentResponse> getMySchedule(UUID userId);
    AppointmentResponse callNextPatient(UUID userId);
    AppointmentResponse updateAppointmentStatus(
            UUID userId,
            UUID appointmentId,
            AppointmentStatus newStatus
    );
}
