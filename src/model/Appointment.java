package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Appointment class representing medical appointments in the system.
 * Implements encapsulation and provides appointment management functionality.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class Appointment {
    private String appointmentId;
    private String doctorId;
    private String patientId;
    private LocalDate date;
    private LocalTime time;
    private String timeSlot;
    private String reason;
    private String status;
    private String notes;
    private LocalDate createdDate;
    
    /**
     * Constructor for Appointment class
     * @param appointmentId unique appointment identifier
     * @param doctorId ID of the doctor
     * @param patientId ID of the patient
     * @param date appointment date
     * @param time appointment time
     * @param timeSlot time slot for the appointment
     * @param reason reason for the appointment
     */
    public Appointment(String appointmentId, String doctorId, String patientId, 
                       LocalDate date, LocalTime time, String timeSlot, String reason) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.time = time;
        this.timeSlot = timeSlot;
        this.reason = reason;
        this.status = "PENDING"; // Set to PENDING initially, will be SCHEDULED when booked
        this.notes = "";
        this.createdDate = LocalDate.now();
    }
    
    // Getters and Setters (Encapsulation)
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
    
    /**
     * Book the appointment
     * @return true if successful, false otherwise
     */
    public boolean book() {
        if (status.equals("SCHEDULED")) {
            System.out.println("Appointment " + appointmentId + " is already booked.");
            return false;
        }
        this.status = "SCHEDULED";
        System.out.println("Appointment " + appointmentId + " booked successfully.");
        return true;
    }
    
    /**
     * Cancel the appointment
     * @return true if successful, false otherwise
     */
    public boolean cancel() {
        if (status.equals("CANCELLED")) {
            System.out.println("Appointment " + appointmentId + " is already cancelled.");
            return false;
        }
        this.status = "CANCELLED";
        System.out.println("Appointment " + appointmentId + " cancelled successfully.");
        return true;
    }
    
    /**
     * Approve the appointment (doctor approves pending appointment)
     * @return true if successful, false otherwise
     */
    public boolean approve() {
        if (status.equals("APPROVED")) {
            System.out.println("Appointment " + appointmentId + " is already approved.");
            return false;
        }
        if (status.equals("CANCELLED")) {
            System.out.println("Cannot approve a cancelled appointment.");
            return false;
        }
        this.status = "APPROVED";
        System.out.println("Appointment " + appointmentId + " approved successfully.");
        return true;
    }
    
    /**
     * Reject the appointment (doctor rejects pending appointment)
     * @return true if successful, false otherwise
     */
    public boolean reject() {
        if (status.equals("REJECTED")) {
            System.out.println("Appointment " + appointmentId + " is already rejected.");
            return false;
        }
        if (status.equals("COMPLETED")) {
            System.out.println("Cannot reject a completed appointment.");
            return false;
        }
        this.status = "REJECTED";
        System.out.println("Appointment " + appointmentId + " rejected.");
        return true;
    }
    
    /**
     * Complete the appointment
     * @return true if successful, false otherwise
     */
    public boolean complete() {
        if (status.equals("COMPLETED")) {
            System.out.println("Appointment " + appointmentId + " is already completed.");
            return false;
        }
        this.status = "COMPLETED";
        System.out.println("Appointment " + appointmentId + " completed successfully.");
        return true;
    }
    
    /**
     * Reschedule the appointment
     * @param newDate new appointment date
     * @param newTime new appointment time
     * @param newTimeSlot new time slot
     * @return true if successful, false otherwise
     */
    public boolean reschedule(LocalDate newDate, LocalTime newTime, String newTimeSlot) {
        if (status.equals("CANCELLED")) {
            System.out.println("Cannot reschedule a cancelled appointment.");
            return false;
        }
        this.date = newDate;
        this.time = newTime;
        this.timeSlot = newTimeSlot;
        this.status = "RESCHEDULED";
        System.out.println("Appointment " + appointmentId + " rescheduled successfully.");
        return true;
    }
    
    /**
     * Add notes to the appointment
     * @param newNotes notes to add
     */
    public void addNotes(String newNotes) {
        if (notes.isEmpty()) {
            notes = newNotes;
        } else {
            notes += "\n" + newNotes;
        }
        System.out.println("Notes added to appointment " + appointmentId);
    }
    
    /**
     * Check if appointment is in the past
     * @return true if appointment is in the past, false otherwise
     */
    public boolean isPastAppointment() {
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if appointment is today
     * @return true if appointment is today, false otherwise
     */
    public boolean isToday() {
        return date.equals(LocalDate.now());
    }
    
    /**
     * Check if appointment is in the future
     * @return true if appointment is in the future, false otherwise
     */
    public boolean isFutureAppointment() {
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Display appointment information
     */
    public void displayAppointment() {
        System.out.println("=== APPOINTMENT INFORMATION ===");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("Time Slot: " + timeSlot);
        System.out.println("Reason: " + reason);
        System.out.println("Status: " + status);
        System.out.println("Notes: " + notes);
        System.out.println("Created Date: " + createdDate);
    }
    
    /**
     * Get formatted appointment summary
     * @return String containing appointment summary
     */
    public String getAppointmentSummary() {
        return String.format("ID: %s | Date: %s | Time: %s | Status: %s | Reason: %s", 
                           appointmentId, date, time, status, reason);
    }
    
    /**
     * Returns formatted string representation of appointment
     * @return String containing appointment information
     */
    @Override
    public String toString() {
        return String.format("Appointment[ID: %s, Doctor: %s, Patient: %s, Date: %s, Time: %s, Status: %s]", 
                           appointmentId, doctorId, patientId, date, time, status);
    }
}
