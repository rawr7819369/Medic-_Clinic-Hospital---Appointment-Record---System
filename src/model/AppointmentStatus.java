package model;

/**
 * Enumeration of valid appointment statuses. Stored in DB as uppercase strings via name().
 */
public enum AppointmentStatus {
    PENDING,
    SCHEDULED,
    CONFIRMED,
    REJECTED,
    CANCELLED,
    COMPLETED,
    RESCHEDULED
}
