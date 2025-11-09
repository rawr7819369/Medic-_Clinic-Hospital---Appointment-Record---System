package ui;

import model.*;
import service.*;
import util.ConsolePrinter;
import util.InputValidator;
import util.DataStore;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PatientHomeScreen class for handling patient-specific functionality.
 * Implements patient home screen with appointment booking, viewing, and profile management.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class PatientHomeScreen {
    private final AppointmentService appointmentService;
    private final AccessibilityService accessibilityService;
    private final DatabaseService dbService = new DatabaseService();
    private final DataStore dataStore = DataStore.getInstance();
    private final PrescriptionService prescriptionService = new PrescriptionService(DataStore.getInstance());
    private Patient currentPatient;
    
    /**
     * Constructor for PatientHomeScreen
     * @param appointmentService appointment service instance
     * @param accessibilityService accessibility service instance
     */
    public PatientHomeScreen(AppointmentService appointmentService, AccessibilityService accessibilityService) {
        this.appointmentService = appointmentService;
        this.accessibilityService = accessibilityService;
    }
    
    /**
     * Display patient home screen and handle patient operations
     * @param patient logged in patient user
     */
    public void displayPatientHomeScreen(Patient patient) {
        this.currentPatient = patient;
        
        ConsolePrinter.clearScreen();
        ConsolePrinter.printHeader("Patient Home");
        ConsolePrinter.printInfo("Welcome, " + patient.getFullName() + "!");
        ConsolePrinter.printInfo("Patient ID: " + patient.getPatientId());
        ConsolePrinter.printInfo("Age: " + patient.getAge() + " | Gender: " + patient.getGender());
        
        while (true) {
            ConsolePrinter.printSubHeader("Patient Menu");
            
            String[] menuOptions = {
                "Book Appointment",
                "View Appointments",
                "Medical History",
                "Profile Settings",
                "Accessibility Settings",
                "Logout"
            };
            
            ConsolePrinter.printMenu("Select an option", menuOptions);
            
            int choice = ConsolePrinter.getIntInput("Enter your choice", 1, 6);
            
            switch (choice) {
                case 1:
                    handleBookAppointment();
                    break;
                case 2:
                    handleViewAppointments();
                    break;
                case 3:
                    handleMedicalHistory();
                    break;
                case 4:
                    handleProfileSettings();
                    break;
                case 5:
                    handleAccessibilitySettings();
                    break;
                case 6:
                    if (handleLogout()) {
                        return;
                    }
                    break;
                default:
                    ConsolePrinter.printError("Invalid choice. Please try again.");
            }
            
            if (choice != 6) {
                ConsolePrinter.pause("Press Enter to continue");
            }
        }
    }
    
    /**
     * Handle booking an appointment
     */
    private void handleBookAppointment() {
        ConsolePrinter.printSubHeader("Book Appointment");
        
        // Get available doctors
        List<Doctor> doctors = getAvailableDoctors();
        if (doctors.isEmpty()) {
            ConsolePrinter.printWarning("No doctors available for booking.");
            return;
        }
        
        // Display doctors
        String[] headers = {"Doctor ID", "Name", "Specialization", "Contact"};
        List<String[]> data = new java.util.ArrayList<>();
        
        for (Doctor doctor : doctors) {
            data.add(new String[]{
                doctor.getDoctorId(),
                doctor.getFullName(),
                doctor.getSpecialization(),
                doctor.getContactNumber()
            });
        }
        
        ConsolePrinter.printTable(headers, data);
        
        // Get doctor selection
        String doctorId = ConsolePrinter.getInput("Enter Doctor ID");
        
        // Validate doctor
        Doctor selectedDoctor = findDoctorById(doctorId);
        if (selectedDoctor == null) {
            ConsolePrinter.printError("Invalid Doctor ID. Please try again.");
            return;
        }
        
        // Get appointment date
        String dateStr = ConsolePrinter.getInput("Enter appointment date (yyyy-MM-dd)", 
            input -> InputValidator.isValidDate(input) && InputValidator.isFutureDate(input));
        
        LocalDate appointmentDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Get available time slots
        List<String> availableSlots = appointmentService.getAvailableTimeSlots(doctorId, appointmentDate);
        
        if (availableSlots.isEmpty()) {
            ConsolePrinter.printWarning("No available time slots for " + appointmentDate);
            return;
        }
        
        ConsolePrinter.printList("Available Time Slots", availableSlots);
        
        // Get time slot selection
        String timeSlot = ConsolePrinter.getInput("Enter time slot (HH:MM-HH:MM)", 
            input -> InputValidator.isValidTimeSlot(input) && availableSlots.contains(input));
        
        // Get reason for appointment
        String reason = ConsolePrinter.getInput("Enter reason for appointment", 
            input -> InputValidator.isValidAppointmentReason(input));
        
        // Confirm booking
        ConsolePrinter.printSubHeader("Appointment Confirmation");
        ConsolePrinter.printInfo("Doctor: " + selectedDoctor.getFullName());
        ConsolePrinter.printInfo("Date: " + appointmentDate);
        ConsolePrinter.printInfo("Time: " + timeSlot);
        ConsolePrinter.printInfo("Reason: " + reason);
        
        if (ConsolePrinter.printConfirmation("Confirm appointment booking?")) {
            // Book appointment
            Appointment appointment = appointmentService.bookAppointment(doctorId, currentPatient.getPatientId(), 
                                                                        dateStr, timeSlot, reason);
            
            if (appointment != null) {
                ConsolePrinter.printSuccess("Appointment booked successfully!");
                ConsolePrinter.printInfo("Appointment ID: " + appointment.getAppointmentId());
            } else {
                ConsolePrinter.printError("Failed to book appointment. Please try again.");
            }
        } else {
            ConsolePrinter.printInfo("Appointment booking cancelled.");
        }
    }
    
    /**
     * Handle viewing appointments
     */
    private void handleViewAppointments() {
        ConsolePrinter.printSubHeader("View Appointments");
        
        String[] menuOptions = {
            "Upcoming Appointments",
            "Past Appointments",
            "All Appointments",
            "Appointment Details",
            "Cancel Appointment",
            "Back to Patient Menu"
        };
        
        ConsolePrinter.printMenu("Appointment Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 6);
        
        switch (choice) {
            case 1:
                handleUpcomingAppointments();
                break;
            case 2:
                handlePastAppointments();
                break;
            case 3:
                handleAllAppointments();
                break;
            case 4:
                handleAppointmentDetails();
                break;
            case 5:
                handleCancelAppointment();
                break;
            case 6:
                return; // Back to patient menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle upcoming appointments
     */
    private void handleUpcomingAppointments() {
        ConsolePrinter.printSubHeader("Upcoming Appointments");
        
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointmentsByPatient(currentPatient.getPatientId());
        
        if (upcomingAppointments.isEmpty()) {
            ConsolePrinter.printInfo("No upcoming appointments scheduled.");
        } else {
            appointmentService.displayAppointments(upcomingAppointments, "Upcoming Appointments");
        }
    }
    
    /**
     * Handle past appointments
     */
    private void handlePastAppointments() {
        ConsolePrinter.printSubHeader("Past Appointments");
        
        List<Appointment> pastAppointments = appointmentService.getPastAppointmentsByPatient(currentPatient.getPatientId());
        
        if (pastAppointments.isEmpty()) {
            ConsolePrinter.printInfo("No past appointments found.");
        } else {
            appointmentService.displayAppointments(pastAppointments, "Past Appointments");
        }
    }
    
    /**
     * Handle all appointments
     */
    private void handleAllAppointments() {
        ConsolePrinter.printSubHeader("All Appointments");
        
        List<Appointment> allAppointments = appointmentService.getAppointmentsByPatient(currentPatient.getPatientId());
        
        if (allAppointments.isEmpty()) {
            ConsolePrinter.printInfo("No appointments found.");
        } else {
            appointmentService.displayAppointments(allAppointments, "All Appointments");
        }
    }
    
    /**
     * Handle appointment details
     */
    private void handleAppointmentDetails() {
        ConsolePrinter.printSubHeader("Appointment Details");
        
        String appointmentId = ConsolePrinter.getInput("Enter Appointment ID");
        
        appointmentService.displayAppointmentDetails(appointmentId);
    }
    
    /**
     * Handle canceling an appointment
     */
    private void handleCancelAppointment() {
        ConsolePrinter.printSubHeader("Cancel Appointment");
        
        String appointmentId = ConsolePrinter.getInput("Enter Appointment ID to cancel");
        
        if (ConsolePrinter.printConfirmation("Are you sure you want to cancel this appointment?")) {
            if (appointmentService.cancelAppointment(appointmentId)) {
                ConsolePrinter.printSuccess("Appointment cancelled successfully!");
            } else {
                ConsolePrinter.printError("Failed to cancel appointment. Please try again.");
            }
        } else {
            ConsolePrinter.printInfo("Appointment cancellation cancelled.");
        }
    }
    
    /**
     * Handle medical history
     */
    private void handleMedicalHistory() {
        ConsolePrinter.printSubHeader("Medical History");
        
        String[] menuOptions = {
            "Medical History Dashboard",
            "View Medical Records",
            "View Prescriptions",
            "View Allergies",
            "View Current Medications",
            "Back to Patient Menu"
        };
        
        ConsolePrinter.printMenu("Medical History Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 6);
        
        switch (choice) {
            case 1:
                showMedicalHistoryDashboard();
                break;
            case 2:
                handleViewMedicalRecords();
                break;
            case 3:
                handleViewPrescriptions();
                break;
            case 4:
                handleViewAllergies();
                break;
            case 5:
                handleViewCurrentMedications();
                break;
            case 6:
                return; // Back to patient menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle viewing medical records
     */
    private void handleViewMedicalRecords() {
        ConsolePrinter.printSubHeader("Medical Records");
        List<MedicalRecord> records = dataStore.getMedicalRecordsByPatient(currentPatient.getPatientId());
        if (records.isEmpty()) {
            ConsolePrinter.printInfo("No medical records found.");
            return;
        }
        String[] headers = {"Record ID", "Date", "Doctor ID", "Diagnosis", "Status", "Notes"};
        java.util.List<String[]> rows = new java.util.ArrayList<>();
        for (MedicalRecord r : records) {
            rows.add(new String[]{
                r.getRecordId(),
                String.valueOf(r.getRecordDate()),
                r.getDoctorId(),
                r.getDiagnosis(),
                r.getStatus(),
                r.getNotes() != null ? r.getNotes() : ""
            });
        }
        ConsolePrinter.printTable(headers, rows);
    }
    
    /**
     * Handle viewing prescriptions
     */
    private void handleViewPrescriptions() {
        ConsolePrinter.printSubHeader("Prescriptions");
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsForPatient(currentPatient.getPatientId());
        if (prescriptions.isEmpty()) {
            ConsolePrinter.printInfo("No prescriptions found.");
            return;
        }
        prescriptionService.displayPrescriptions(prescriptions, "Your Prescriptions");
    }
    
    /**
     * Handle viewing allergies
     */
    private void handleViewAllergies() {
        ConsolePrinter.printSubHeader("Allergies");
        
        List<String> allergies = currentPatient.getAllergies();
        
        if (allergies.isEmpty()) {
            ConsolePrinter.printInfo("No allergies recorded.");
        } else {
            ConsolePrinter.printList("Recorded Allergies", allergies);
        }
    }
    
    /**
     * Handle viewing current medications
     */
    private void handleViewCurrentMedications() {
        ConsolePrinter.printSubHeader("Current Medications");
        
        List<String> medications = currentPatient.getCurrentMedications();
        
        if (medications.isEmpty()) {
            ConsolePrinter.printInfo("No current medications recorded.");
        } else {
            ConsolePrinter.printList("Current Medications", medications);
        }
    }
    
    /**
     * Handle profile settings
     */
    private void handleProfileSettings() {
        ConsolePrinter.printSubHeader("Profile Settings");
        
        String[] menuOptions = {
            "View Profile",
            "Update Contact Information",
            "Update Address",
            "Update Password",
            "Add Allergy",
            "Remove Allergy",
            "Add Current Medication",
            "Remove Current Medication",
            "Back to Patient Menu"
        };
        
        ConsolePrinter.printMenu("Profile Settings Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 9);
        
        switch (choice) {
            case 1:
                handleViewProfile();
                break;
            case 2:
                handleUpdateContactInfo();
                break;
            case 3:
                handleUpdateAddress();
                break;
            case 4:
                handleUpdatePassword();
                break;
            case 5:
                handleAddAllergy();
                break;
            case 6:
                handleRemoveAllergy();
                break;
            case 7:
                handleAddCurrentMedication();
                break;
            case 8:
                handleRemoveCurrentMedication();
                break;
            case 9:
                return; // Back to patient menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle viewing profile
     */
    private void handleViewProfile() {
        ConsolePrinter.printSubHeader("Profile Information");
        currentPatient.displayInfo();
    }
    
    /**
     * Handle updating contact information
     */
    private void handleUpdateContactInfo() {
        ConsolePrinter.printSubHeader("Update Contact Information");
        
        String newEmail = ConsolePrinter.getInput("Enter new email address", 
            input -> InputValidator.isValidEmail(input));
        
        String newContact = ConsolePrinter.getInput("Enter new contact number", 
            input -> InputValidator.isValidPhone(input));
        
        boolean dbOk = dbService.updateUserContact(currentPatient.getUsername(), newEmail, newContact, currentPatient.getAddress());
        if (dbOk) {
            currentPatient.updateProfile(newEmail, newContact, currentPatient.getAddress());
            ConsolePrinter.printSuccess("Contact information updated successfully!");
        } else {
            ConsolePrinter.printError("Failed to update contact information.");
        }
    }
    
    /**
     * Handle updating address
     */
    private void handleUpdateAddress() {
        ConsolePrinter.printSubHeader("Update Address");
        
        String newAddress = ConsolePrinter.getInput("Enter new address", 
            input -> InputValidator.isNotEmpty(input));
        
        boolean dbOk = dbService.updateUserContact(currentPatient.getUsername(), currentPatient.getEmail(), currentPatient.getContactNumber(), newAddress);
        if (dbOk) {
            currentPatient.setAddress(newAddress);
            ConsolePrinter.printSuccess("Address updated successfully!");
        } else {
            ConsolePrinter.printError("Failed to update address.");
        }
    }

    /**
     * Handle updating password
     */
    private void handleUpdatePassword() {
        ConsolePrinter.printSubHeader("Update Password");
        String current = ConsolePrinter.getPasswordInput("Enter current password");
        if (!currentPatient.getPassword().equals(current)) {
            ConsolePrinter.printError("Current password is incorrect.");
            return;
        }
        String newPass = ConsolePrinter.getPasswordInput("Enter new password");
        String confirm = ConsolePrinter.getPasswordInput("Confirm new password");
        if (!InputValidator.isNotEmpty(newPass) || !newPass.equals(confirm)) {
            ConsolePrinter.printError("Passwords do not match or are invalid.");
            return;
        }
        boolean dbOk = dbService.updateUserPassword(currentPatient.getUsername(), newPass);
        if (dbOk) {
            currentPatient.setPassword(newPass);
            ConsolePrinter.printSuccess("Password updated successfully!");
        } else {
            ConsolePrinter.printError("Failed to update password.");
        }
    }
    
    /**
     * Handle adding allergy
     */
    private void handleAddAllergy() {
        ConsolePrinter.printSubHeader("Add Allergy");
        
        String allergy = ConsolePrinter.getInput("Enter allergy", 
            input -> InputValidator.isNotEmpty(input));
        
        currentPatient.addAllergy(allergy);
        boolean dbOk = dbService.updatePatientAllergies(currentPatient.getPatientId(), currentPatient.getAllergies());
        if (dbOk) {
            ConsolePrinter.printSuccess("Allergy added successfully!");
        } else {
            ConsolePrinter.printWarning("Allergy added locally, but failed to persist to database.");
        }
    }
    
    /**
     * Handle removing allergy
     */
    private void handleRemoveAllergy() {
        ConsolePrinter.printSubHeader("Remove Allergy");
        
        List<String> allergies = currentPatient.getAllergies();
        if (allergies.isEmpty()) {
            ConsolePrinter.printWarning("No allergies recorded.");
            return;
        }
        
        ConsolePrinter.printList("Current Allergies", allergies);
        
        String allergy = ConsolePrinter.getInput("Enter allergy to remove");
        
        if (currentPatient.getAllergies().contains(allergy)) {
            currentPatient.removeAllergy(allergy);
            boolean dbOk = dbService.updatePatientAllergies(currentPatient.getPatientId(), currentPatient.getAllergies());
            if (dbOk) {
                ConsolePrinter.printSuccess("Allergy removed successfully!");
            } else {
                ConsolePrinter.printWarning("Allergy removed locally, but failed to persist to database.");
            }
        } else {
            ConsolePrinter.printError("Allergy not found.");
        }
    }
    
    /**
     * Handle adding current medication
     */
    private void handleAddCurrentMedication() {
        ConsolePrinter.printSubHeader("Add Current Medication");
        
        String medication = ConsolePrinter.getInput("Enter medication name", 
            input -> InputValidator.isNotEmpty(input));
        
        currentPatient.addCurrentMedication(medication);
        boolean dbOk = dbService.updatePatientMedications(currentPatient.getPatientId(), currentPatient.getCurrentMedications());
        if (dbOk) {
            ConsolePrinter.printSuccess("Current medication added successfully!");
        } else {
            ConsolePrinter.printWarning("Medication added locally, but failed to persist to database.");
        }
    }
    
    /**
     * Handle removing current medication
     */
    private void handleRemoveCurrentMedication() {
        ConsolePrinter.printSubHeader("Remove Current Medication");
        
        List<String> medications = currentPatient.getCurrentMedications();
        if (medications.isEmpty()) {
            ConsolePrinter.printWarning("No current medications recorded.");
            return;
        }
        
        ConsolePrinter.printList("Current Medications", medications);
        
        String medication = ConsolePrinter.getInput("Enter medication to remove");
        
        if (currentPatient.getCurrentMedications().contains(medication)) {
            currentPatient.removeCurrentMedication(medication);
            boolean dbOk = dbService.updatePatientMedications(currentPatient.getPatientId(), currentPatient.getCurrentMedications());
            if (dbOk) {
                ConsolePrinter.printSuccess("Current medication removed successfully!");
            } else {
                ConsolePrinter.printWarning("Medication removed locally, but failed to persist to database.");
            }
        } else {
            ConsolePrinter.printError("Medication not found.");
        }
    }
    
    /**
     * Handle accessibility settings
     */
    private void handleAccessibilitySettings() {
        ConsolePrinter.printSubHeader("Accessibility Settings");
        
        while (true) {
            accessibilityService.displayAccessibilityMenu();
            
            int choice = ConsolePrinter.getIntInput("Select an option", 1, 10);
            
            if (choice == 10) {
                break; // Back to patient menu
            }
            
            accessibilityService.handleAccessibilityChoice(choice);
            ConsolePrinter.pause("Press Enter to continue");
        }
    }
    
    /**
     * Handle logout
     * @return true if user wants to logout, false otherwise
     */
    private boolean handleLogout() {
        if (ConsolePrinter.printConfirmation("Are you sure you want to logout?")) {
            ConsolePrinter.printSuccess("Logged out successfully!");
            return true;
        }
        return false;
    }
    
    /**
     * Get available doctors
     * @return list of available doctors
     */
    private List<Doctor> getAvailableDoctors() {
        // This would typically query the database for available doctors
        // For now, we'll return a sample list
        return new java.util.ArrayList<>();
    }
    
    /**
     * Find doctor by ID
     * @param doctorId doctor ID to search for
     * @return Doctor object or null if not found
     */
    private Doctor findDoctorById(String doctorId) {
        // This would typically query the database for the doctor
        // For now, we'll return null
        return null;
    }

    /**
     * Show medical history dashboard with pagination
     */
    private void showMedicalHistoryDashboard() {
        // Aggregate items: past appointments, medical records, prescriptions, scans
        List<Appointment> appts = appointmentService.getAppointmentsByPatient(currentPatient.getPatientId());
        List<MedicalRecord> records = dataStore.getMedicalRecordsByPatient(currentPatient.getPatientId());
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsForPatient(currentPatient.getPatientId());
        List<Scan> scans = dataStore.getScansByPatient(currentPatient.getPatientId());

        // Build simple timeline strings
        java.util.List<String> items = new java.util.ArrayList<>();
        for (Appointment a : appts) {
            items.add(String.format("[Appointment] %s %s | %s | %s | %s", a.getDate(), a.getTimeSlot(), a.getAppointmentId(), a.getReason(), a.getStatus()));
        }
        for (MedicalRecord r : records) {
            items.add(String.format("[Record] %s | %s | %s | %s", r.getRecordDate(), r.getRecordId(), r.getDiagnosis(), r.getStatus()));
        }
        for (Prescription p : prescriptions) {
            String until = p.getValidUntil() != null ? p.getValidUntil().toString() : "N/A";
            items.add(String.format("[Prescription] %s | %s | Refills: %d | ValidUntil: %s", p.getPrescriptionId(), p.getStatus(), p.getRefillsRemaining(), until));
        }
        for (Scan s : scans) {
            items.add(String.format("[Scan] %s | %s | %s", s.getScanId(), s.getFileType(), s.getFilePath()));
        }

        if (items.isEmpty()) {
            ConsolePrinter.printInfo("No medical history items available.");
            return;
        }

        paginateList(items, 10, "Medical History Timeline");
    }

    /**
     * Simple pagination for lists
     */
    private void paginateList(java.util.List<String> items, int pageSize, String title) {
        int page = 0;
        while (true) {
            ConsolePrinter.printSubHeader(title + String.format(" (Page %d/%d)", page + 1, (items.size() + pageSize - 1) / pageSize));
            int start = page * pageSize;
            int end = Math.min(start + pageSize, items.size());
            for (int i = start; i < end; i++) {
                System.out.println((i + 1) + ". " + items.get(i));
            }
            System.out.println();
            System.out.println("[N] Next  [P] Prev  [Q] Quit");
            String input = ConsolePrinter.getInput("Choose action (N/P/Q)").trim().toLowerCase();
            if (input.equals("n") && end < items.size()) {
                page++;
            } else if (input.equals("p") && page > 0) {
                page--;
            } else if (input.equals("q")) {
                break;
            }
        }
    }
}
