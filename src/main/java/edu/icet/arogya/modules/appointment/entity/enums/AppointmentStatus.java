package edu.icet.arogya.modules.appointment.entity.enums;

import edu.icet.arogya.modules.appointment.entity.Appointment;

import java.time.LocalDateTime;

public enum AppointmentStatus {
    CHECKED_IN {
        @Override
        public void apply(Appointment appointment) {
            appointment.setCheckInTime(LocalDateTime.now());
        }
    },

    IN_PROGRESS {
        @Override
        public void apply(Appointment appointment) {
            appointment.setConsultationStartTime(LocalDateTime.now());
        }
    },

    COMPLETED {
        @Override
        public void apply(Appointment appointment) {
            appointment.setConsultationEndTime(LocalDateTime.now());
            appointment.setCompletedAt(LocalDateTime.now());
        }
    },

    CANCELLED {
        @Override
        public void apply(Appointment appointment) {
            appointment.setCancelledAt(LocalDateTime.now());
        }
    },

    NO_SHOW {
        @Override
        public void apply(Appointment appointment) {}
    },

    PENDING, CONFIRMED;

    public void apply(Appointment appointment) {}
}
