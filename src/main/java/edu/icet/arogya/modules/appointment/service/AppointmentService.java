package edu.icet.arogya.modules.appointment.service;

import edu.icet.arogya.modules.appointment.dto.CreateAppointmentRequest;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import edu.icet.arogya.modules.patient.entity.Patient;

public interface AppointmentService {
    Appointment book(Patient patient, DoctorSchedule schedule, CreateAppointmentRequest request);
    void cancel(Appointment appointment);
    Appointment updateStatus(Appointment appointment, AppointmentStatus newStatus);
}
