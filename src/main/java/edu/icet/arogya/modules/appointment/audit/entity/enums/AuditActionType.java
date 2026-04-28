package edu.icet.arogya.modules.appointment.audit.entity.enums;

public enum AuditActionType {
    // Patient actions
    APPOINTMENT_BOOKED,
    APPOINTMENT_CANCELLED_BY_PATIENT,

    // Doctor actions
    APPOINTMENT_STATUS_UPDATED,
    APPOINTMENT_COMPLETED,

    // Admin actions
    APPOINTMENT_STATUS_OVERRIDDEN,
    APPOINTMENT_CANCELLED_BY_ADMIN,
    APPOINTMENT_BULK_CANCELLED
}
