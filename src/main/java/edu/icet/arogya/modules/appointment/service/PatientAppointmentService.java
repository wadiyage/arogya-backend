package edu.icet.arogya.modules.appointment.service;

import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.dto.CreateAppointmentRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PatientAppointmentService {
    AppointmentResponse bookAppointment(UUID userId, CreateAppointmentRequest request);
    Page<@NonNull AppointmentResponse> getMyAppointments(UUID userId, Pageable pageable);
    void cancelAppointment(UUID userId, UUID appointmentId);
}
