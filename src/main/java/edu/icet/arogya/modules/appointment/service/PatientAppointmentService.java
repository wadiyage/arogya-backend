package edu.icet.arogya.modules.appointment.service;

import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.dto.CreateAppointmentRequest;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;

import java.util.List;
import java.util.UUID;

public interface PatientAppointmentService {
    AppointmentResponse bookAppointment(UUID userId, CreateAppointmentRequest request);
    List<AppointmentResponse> getMyAppointments(UUID userId);
    void cancelAppointment(UUID userId, UUID appointmentId);
}
