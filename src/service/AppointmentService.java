package service;

import model.*;
import util.DataStore;
import util.InputValidator;
import util.ConsolePrinter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * AppointmentService class for handling appointment-related operations.
 * Implements business logic for appointment booking, management, and scheduling.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class AppointmentService {
    private final DataStore dataStore;
    private final DatabaseService databaseService = new DatabaseService();
    
    /**
     * Constructor for AppointmentService
     * @param dataStore data store instance
     */
    public AppointmentService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Doctor-initiated appointment creation
     */
    public Appointment createAppointmentByDoctor(String doctorId, String patientId, String date, String timeSlot, String reason) {
        return bookAppointment(doctorId, patientId, date, timeSlot, reason);
    }
    
    /**
     * Book a new appointment
     * @param doctorId ID of the doctor
     * @param patientId ID of the patient
     * @param date appointment date
     * @param timeSlot time slot for the appointment
     * @param reason reason for the appointment
     * @return Appointment object if booking successful, null otherwise
     */
    public Appointment bookAppointment(String doctorId, String patientId, String date, 
                                       String timeSlot, String reason) {
        
        // Validate inputs
        if (!validateAppointmentInputs(doctorId, patientId, date, timeSlot, reason)) {
            return null;
        }
        
        // Check if doctor exists
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            ConsolePrinter.printError("Doctor not found");
            return null;
        }
        
        // Check if patient exists
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            ConsolePrinter.printError("Patient not found");
            return null;
        }
        
        // Parse date
        LocalDate appointmentDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Check if time slot is available
        if (!dataStore.isTimeSlotAvailable(doctorId, appointmentDate, timeSlot)) {
            ConsolePrinter.printError("Time slot is not available");
            return null;
        }
        
        // Check if doctor is available at the specified time
        if (!doctor.isAvailableAt(timeSlot)) {
            ConsolePrinter.printError("Doctor is not available at the specified time slot");
            return null;
        }
        
        // Generate unique appointment ID
        String appointmentId = dataStore.generateAppointmentId();
        
        // Parse time from time slot
        String[] timeParts = timeSlot.split("-");
        LocalTime appointmentTime = LocalTime.parse(timeParts[0]);
        
        // Create new appointment (defaults to PENDING for approval)
        Appointment appointment = new Appointment(appointmentId, doctorId, patientId, 
                                                appointmentDate, appointmentTime, timeSlot, reason);
        
        // Save appointment as PENDING (awaiting approval)
        if (dataStore.addAppointment(appointment)) {
            ConsolePrinter.printSuccess("Appointment request created (Pending Approval)");
            ConsolePrinter.printInfo("Appointment ID: " + appointmentId);
            ConsolePrinter.printInfo("Date: " + appointmentDate);
            ConsolePrinter.printInfo("Time: " + timeSlot);
            ConsolePrinter.printInfo("Doctor: " + doctor.getFullName());
            ConsolePrinter.printInfo("Patient: " + patient.getFullName());
            return appointment;
        } else {
            ConsolePrinter.printError("Failed to save appointment");
            return null;
        }
    }
    
    /**
     * Cancel an appointment
     * @param appointmentId ID of the appointment to cancel
     * @return true if cancellation successful, false otherwise
     */
    public boolean cancelAppointment(String appointmentId) {
        return cancelAppointment(appointmentId, null);
    }

    /**
     * Cancel an appointment with a reason (doctor-driven)
     */
    public boolean cancelAppointment(String appointmentId, String reason) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            ConsolePrinter.printError("Appointment not found");
            return false;
        }
        boolean dbOk = databaseService.updateAppointmentStatus(appointmentId, "CANCELLED", reason);
        if (!dbOk) {
            ConsolePrinter.printWarning("Database not updated; proceeding in-memory");
        }
        appointment.setStatus("CANCELLED");
        if (reason != null && !reason.trim().isEmpty()) {
            appointment.addNotes("Cancellation reason: " + reason);
        }
        ConsolePrinter.printSuccess("Appointment cancelled");
        return true;
    }
    
    /**
     * Approve an appointment (doctor approves pending appointment)
     * @param appointmentId ID of the appointment to approve
     * @return true if approval successful, false otherwise
     */
    public boolean approveAppointment(String appointmentId) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            ConsolePrinter.printError("Appointment not found");
            return false;
        }
        // Business rule: map approval to CONFIRMED per requirement
        boolean dbOk = databaseService.updateAppointmentStatus(appointmentId, "CONFIRMED", null);
        if (!dbOk) {
            ConsolePrinter.printWarning("Database not updated; proceeding in-memory");
        }
        appointment.setStatus("CONFIRMED");
        ConsolePrinter.printSuccess("Appointment approved (confirmed)");
        return true;
    }
    
    /**
     * Reject an appointment (doctor rejects pending appointment)
     * @param appointmentId ID of the appointment to reject
     * @param reason reason for rejection
     * @return true if rejection successful, false otherwise
     */
    public boolean rejectAppointment(String appointmentId, String reason) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            ConsolePrinter.printError("Appointment not found");
            return false;
        }
        boolean dbOk = databaseService.updateAppointmentStatus(appointmentId, "REJECTED", reason);
        if (!dbOk) {
            ConsolePrinter.printWarning("Database not updated; proceeding in-memory");
        }
        appointment.setStatus("REJECTED");
        if (reason != null && !reason.trim().isEmpty()) {
            appointment.addNotes("Rejection reason: " + reason);
        }
        ConsolePrinter.printSuccess("Appointment rejected");
        return true;
    }
    
    /**
     * Complete an appointment
     * @param appointmentId ID of the appointment to complete
     * @return true if completion successful, false otherwise
     */
    public boolean completeAppointment(String appointmentId) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            ConsolePrinter.printError("Appointment not found");
            return false;
        }
        
        if (appointment.complete()) {
            ConsolePrinter.printSuccess("Appointment completed successfully");
            return true;
        } else {
            ConsolePrinter.printError("Failed to complete appointment");
            return false;
        }
    }
    
    /**
     * Reschedule an appointment
     * @param appointmentId ID of the appointment to reschedule
     * @param newDate new appointment date
     * @param newTimeSlot new time slot
     * @return true if rescheduling successful, false otherwise
     */
    public boolean rescheduleAppointment(String appointmentId, String newDate, String newTimeSlot) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            ConsolePrinter.printError("Appointment not found");
            return false;
        }
        
        // Validate new date and time slot
        if (!InputValidator.isValidDate(newDate) || !InputValidator.isValidTimeSlot(newTimeSlot)) {
            ConsolePrinter.printError("Invalid date or time slot format");
            return false;
        }
        
        // Check if new time slot is available
        LocalDate appointmentDate = LocalDate.parse(newDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (!dataStore.isTimeSlotAvailable(appointment.getDoctorId(), appointmentDate, newTimeSlot)) {
            ConsolePrinter.printError("New time slot is not available");
            return false;
        }
        
        // Parse new time
        String[] timeParts = newTimeSlot.split("-");
        LocalTime newTime = LocalTime.parse(timeParts[0]);
        
        if (appointment.reschedule(appointmentDate, newTime, newTimeSlot)) {
            ConsolePrinter.printSuccess("Appointment rescheduled successfully");
            return true;
        } else {
            ConsolePrinter.printError("Failed to reschedule appointment");
            return false;
        }
    }
    
    /**
     * Get appointments by doctor ID
     * @param doctorId ID of the doctor
     * @return list of appointments for the doctor
     */
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return dataStore.getAppointmentsByDoctor(doctorId);
    }
    
    /**
     * Get appointments by patient ID
     * @param patientId ID of the patient
     * @return list of appointments for the patient
     */
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return dataStore.getAppointmentsByPatient(patientId);
    }
    
    /**
     * Get all appointments
     * @return list of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(dataStore.getAllAppointments());
    }
    
    /**
     * Get appointments by status
     * @param status appointment status
     * @return list of appointments with the specified status
     */
    public List<Appointment> getAppointmentsByStatus(String status) {
        return dataStore.getAllAppointments().stream()
            .filter(appointment -> appointment.getStatus().equals(status.toUpperCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get pending appointments for a doctor to approve
     * @param doctorId ID of the doctor
     * @return list of pending appointments for the doctor
     */
    public List<Appointment> getPendingAppointmentsForDoctor(String doctorId) {
        return dataStore.getAppointmentsByDoctor(doctorId).stream()
            .filter(appointment -> appointment.getStatus().equals("PENDING") || appointment.getStatus().equals("SCHEDULED"))
            .collect(Collectors.toList());
    }
    
    /**
     * Get approved appointments for a patient
     * @param patientId ID of the patient
     * @return list of approved appointments for the patient
     */
    public List<Appointment> getApprovedAppointmentsForPatient(String patientId) {
        return dataStore.getAppointmentsByPatient(patientId).stream()
            .filter(appointment -> appointment.getStatus().equals("APPROVED"))
            .collect(Collectors.toList());
    }
    
    /**
     * Get appointments by date
     * @param date appointment date
     * @return list of appointments on the specified date
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return dataStore.getAllAppointments().stream()
            .filter(appointment -> appointment.getDate().equals(date))
            .collect(Collectors.toList());
    }
    
    /**
     * Get upcoming appointments for a doctor
     * @param doctorId ID of the doctor
     * @return list of upcoming appointments
     */
    public List<Appointment> getUpcomingAppointmentsByDoctor(String doctorId) {
        LocalDate today = LocalDate.now();
        return dataStore.getAppointmentsByDoctor(doctorId).stream()
            .filter(appointment -> appointment.getDate().isAfter(today) || appointment.getDate().equals(today))
            .filter(appointment -> !appointment.getStatus().equals("CANCELLED"))
            .collect(Collectors.toList());
    }
    
    /**
     * Get upcoming appointments for a patient
     * @param patientId ID of the patient
     * @return list of upcoming appointments
     */
    public List<Appointment> getUpcomingAppointmentsByPatient(String patientId) {
        LocalDate today = LocalDate.now();
        return dataStore.getAppointmentsByPatient(patientId).stream()
            .filter(appointment -> appointment.getDate().isAfter(today) || appointment.getDate().equals(today))
            .filter(appointment -> !appointment.getStatus().equals("CANCELLED"))
            .collect(Collectors.toList());
    }
    
    /**
     * Get past appointments for a patient
     * @param patientId ID of the patient
     * @return list of past appointments
     */
    public List<Appointment> getPastAppointmentsByPatient(String patientId) {
        LocalDate today = LocalDate.now();
        return dataStore.getAppointmentsByPatient(patientId).stream()
            .filter(appointment -> appointment.getDate().isBefore(today))
            .collect(Collectors.toList());
    }
    
    /**
     * Display appointments in a formatted table
     * @param appointments list of appointments to display
     * @param title title for the display
     */
    public void displayAppointments(List<Appointment> appointments, String title) {
        if (appointments.isEmpty()) {
            ConsolePrinter.printWarning("No appointments found");
            return;
        }
        
        ConsolePrinter.printSubHeader(title);
        
        String[] headers = {"ID", "Date", "Time", "Doctor", "Patient", "Status", "Reason"};
        List<String[]> data = new ArrayList<>();
        
        for (Appointment appointment : appointments) {
            Doctor doctor = findDoctorById(appointment.getDoctorId());
            Patient patient = findPatientById(appointment.getPatientId());
            
            String doctorName = doctor != null ? doctor.getFullName() : "Unknown";
            String patientName = patient != null ? patient.getFullName() : "Unknown";
            
            data.add(new String[]{
                appointment.getAppointmentId(),
                appointment.getDate().toString(),
                appointment.getTimeSlot(),
                doctorName,
                patientName,
                appointment.getStatus(),
                appointment.getReason()
            });
        }
        
        ConsolePrinter.printTable(headers, data);
    }
    
    /**
     * Display appointment details
     * @param appointmentId ID of the appointment
     */
    public void displayAppointmentDetails(String appointmentId) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            ConsolePrinter.printError("Appointment not found");
            return;
        }
        
        Doctor doctor = findDoctorById(appointment.getDoctorId());
        Patient patient = findPatientById(appointment.getPatientId());
        
        ConsolePrinter.printSubHeader("Appointment Details");
        ConsolePrinter.printInfo("Appointment ID: " + appointment.getAppointmentId());
        ConsolePrinter.printInfo("Date: " + appointment.getDate());
        ConsolePrinter.printInfo("Time: " + appointment.getTimeSlot());
        ConsolePrinter.printInfo("Status: " + appointment.getStatus());
        ConsolePrinter.printInfo("Reason: " + appointment.getReason());
        
        if (doctor != null) {
            ConsolePrinter.printInfo("Doctor: " + doctor.getFullName() + " (" + doctor.getSpecialization() + ")");
        }
        
        if (patient != null) {
            ConsolePrinter.printInfo("Patient: " + patient.getFullName());
        }
        
        if (!appointment.getNotes().isEmpty()) {
            ConsolePrinter.printInfo("Notes: " + appointment.getNotes());
        }
    }
    
    /**
     * Add notes to an appointment
     * @param appointmentId ID of the appointment
     * @param notes notes to add
     * @return true if successful, false otherwise
     */
    public boolean addAppointmentNotes(String appointmentId, String notes) {
        Appointment appointment = dataStore.getAppointment(appointmentId);
        if (appointment == null) {
            ConsolePrinter.printError("Appointment not found");
            return false;
        }
        
        if (InputValidator.isNotEmpty(notes)) {
            appointment.addNotes(notes);
            ConsolePrinter.printSuccess("Notes added to appointment");
            return true;
        } else {
            ConsolePrinter.printError("Notes cannot be empty");
            return false;
        }
    }
    
    /**
     * Get available time slots for a doctor on a specific date
     * @param doctorId ID of the doctor
     * @param date date to check
     * @return list of available time slots
     */
    public List<String> getAvailableTimeSlots(String doctorId, LocalDate date) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            return new ArrayList<>();
        }
        
        List<String> availableSlots = new ArrayList<>();
        List<String> doctorSlots = doctor.getAvailableTimeSlots();
        
        for (String slot : doctorSlots) {
            if (dataStore.isTimeSlotAvailable(doctorId, date, slot)) {
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }
    
    /**
     * Validate appointment inputs
     * @param doctorId doctor ID to validate
     * @param patientId patient ID to validate
     * @param date date to validate
     * @param timeSlot time slot to validate
     * @param reason reason to validate
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateAppointmentInputs(String doctorId, String patientId, String date, 
                                            String timeSlot, String reason) {
        boolean isValid = true;
        
        if (!InputValidator.isNotEmpty(doctorId)) {
            ConsolePrinter.printError("Doctor ID cannot be empty");
            isValid = false;
        }
        
        if (!InputValidator.isNotEmpty(patientId)) {
            ConsolePrinter.printError("Patient ID cannot be empty");
            isValid = false;
        }
        
        if (!InputValidator.isValidDate(date)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Date", "date"));
            isValid = false;
        }
        
        if (!InputValidator.isFutureDate(date)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Date", "futuredate"));
            isValid = false;
        }
        
        if (!InputValidator.isValidTimeSlot(timeSlot)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Time Slot", "timeslot"));
            isValid = false;
        }
        
        if (!InputValidator.isValidAppointmentReason(reason)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Reason", "appointmentreason"));
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Find doctor by ID
     * @param doctorId doctor ID to search for
     * @return Doctor object or null if not found
     */
    private Doctor findDoctorById(String doctorId) {
        for (Doctor doctor : dataStore.getAllDoctors()) {
            if (doctor.getDoctorId().equals(doctorId)) {
                return doctor;
            }
        }
        return null;
    }
    
    /**
     * Find patient by ID
     * @param patientId patient ID to search for
     * @return Patient object or null if not found
     */
    private Patient findPatientById(String patientId) {
        for (Patient patient : dataStore.getAllPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                return patient;
            }
        }
        return null;
    }
    
    /**
     * Get appointment statistics
     * @return map containing appointment statistics
     */
    public java.util.Map<String, Object> getAppointmentStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        // Use DataStore DB-backed counters when available (fallback to in-memory automatically)
        stats.put("totalAppointments", dataStore.countTotalAppointments());
        stats.put("scheduledAppointments", dataStore.countAppointmentsByStatus("SCHEDULED"));
        stats.put("completedAppointments", dataStore.countAppointmentsByStatus("COMPLETED"));
        stats.put("cancelledAppointments", dataStore.countAppointmentsByStatus("CANCELLED"));
        stats.put("rescheduledAppointments", dataStore.countAppointmentsByStatus("RESCHEDULED"));
        
        return stats;
    }
    
    /**
     * Display appointment statistics
     */
    public void displayAppointmentStatistics() {
        java.util.Map<String, Object> stats = getAppointmentStatistics();
        ConsolePrinter.printSubHeader("Appointment Statistics");
        ConsolePrinter.printInfo("Total Appointments: " + stats.get("totalAppointments"));
        ConsolePrinter.printInfo("Scheduled: " + stats.get("scheduledAppointments"));
        ConsolePrinter.printInfo("Completed: " + stats.get("completedAppointments"));
        ConsolePrinter.printInfo("Cancelled: " + stats.get("cancelledAppointments"));
        ConsolePrinter.printInfo("Rescheduled: " + stats.get("rescheduledAppointments"));
    }
}
