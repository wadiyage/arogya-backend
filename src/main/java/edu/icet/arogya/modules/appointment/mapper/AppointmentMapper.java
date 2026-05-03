package edu.icet.arogya.modules.appointment.mapper;

import edu.icet.arogya.modules.appointment.dto.AppointmentResponse;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())

                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getFullName())

                .doctorId(appointment.getSchedule().getDoctor().getId())
                .doctorName(appointment.getSchedule().getDoctor().getFullName())

                .scheduleId(appointment.getSchedule().getId())
                .locationName(appointment.getSchedule().getLocation().getName())
                .scheduleDate(appointment.getSchedule().getScheduleDate())

                .tokenNumber(appointment.getTokenNumber())

                .status(appointment.getStatus())

                .reason(appointment.getReason())
                .notes(appointment.getNotes())

                .checkInTime(appointment.getCheckInTime())
                .consultationStartTime(appointment.getConsultationStartTime())
                .consultationEndTime(appointment.getConsultationEndTime())

                .createdAt(appointment.getCreatedAt())
                .build();
    }
}
