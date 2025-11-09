package ui;

import model.*;
import service.*;
import util.ConsolePrinter;
import util.InputValidator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DoctorDashboard class for handling doctor-specific functionality.
 * Implements doctor dashboard with appointment management, patient records, and prescriptions.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class DoctorDashboard {
    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    private final AccessibilityService accessibilityService;
    private Doctor currentDoctor;
    
    /**
     * Constructor for DoctorDashboard
     * @param appointmentService appointment service instance
     * @param prescriptionService prescription service instance
     * @param accessibilityService accessibility service instance
     */
    public DoctorDashboard(AppointmentService appointmentService, PrescriptionService prescriptionService, AccessibilityService accessibilityService) {
        this.appointmentService = appointmentService;
        this.prescriptionService = prescriptionService;
        this.accessibilityService = accessibilityService;
    }
    
    /**
     * Display doctor dashboard and handle doctor operations
     * @param doctor logged in doctor user
     */
    public void displayDoctorDashboard(Doctor doctor) {
        this.currentDoctor = doctor;
        
        ConsolePrinter.clearScreen();
        ConsolePrinter.printHeader("Doctor Dashboard");
        ConsolePrinter.printInfo("Welcome, Dr. " + doctor.getFullName() + "!");
        ConsolePrinter.printInfo("Specialization: " + doctor.getSpecialization());
        ConsolePrinter.printInfo("Doctor ID: " + doctor.getDoctorId());
        
        while (true) {
            ConsolePrinter.printSubHeader("Doctor Menu");
            
            String[] menuOptions = {
                "View Schedule",
                "Approve/Reject Appointments",
                "Patient List",
                "Medical Records",
                "Create Prescription",
                "View Prescriptions",
                "View Appointments",
                "Accessibility Settings",
                "Logout"
            };
            
            ConsolePrinter.printMenu("Select an option", menuOptions);
            
            int choice = ConsolePrinter.getIntInput("Enter your choice", 1, 9);
            
            switch (choice) {
                case 1:
                    handleViewSchedule();
                    break;
                case 2:
                    handleApproveRejectAppointments();
                    break;
                case 3:
                    handlePatientList();
                    break;
                case 4:
                    handleMedicalRecords();
                    break;
                case 5:
                    handleCreatePrescription();
                    break;
                case 6:
                    handleViewPrescriptions();
                    break;
                case 7:
                    handleViewAppointments();
                    break;
                case 8:
                    handleAccessibilitySettings();
                    break;
                case 9:
                    if (handleLogout()) {
                        return;
                    }
                    break;
                default:
                    ConsolePrinter.printError("Invalid choice. Please try again.");
            }
            
            if (choice != 9) {
                ConsolePrinter.pause("Press Enter to continue");
            }
        }
    }
    
    /**
     * Handle viewing doctor's schedule
     */
    private void handleViewSchedule() {
        ConsolePrinter.printSubHeader("Doctor Schedule");
        
        String[] menuOptions = {
            "Today's Appointments",
            "Upcoming Appointments",
            "All Appointments",
            "Available Time Slots",
            "Back to Doctor Menu"
        };
        
        ConsolePrinter.printMenu("Schedule Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 5);
        
        switch (choice) {
            case 1:
                handleTodaysAppointments();
                break;
            case 2:
                handleUpcomingAppointments();
                break;
            case 3:
                handleAllAppointments();
                break;
            case 4:
                handleAvailableTimeSlots();
                break;
            case 5:
                return; // Back to doctor menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle today's appointments
     */
    private void handleTodaysAppointments() {
        ConsolePrinter.printSubHeader("Today's Appointments");
        
        List<Appointment> todaysAppointments = appointmentService.getAppointmentsByDoctor(currentDoctor.getDoctorId())
            .stream()
            .filter(appointment -> appointment.getDate().equals(LocalDate.now()))
            .collect(java.util.stream.Collectors.toList());
        
        if (todaysAppointments.isEmpty()) {
            ConsolePrinter.printInfo("No appointments scheduled for today.");
        } else {
            appointmentService.displayAppointments(todaysAppointments, "Today's Appointments");
        }
    }
    
    /**
     * Handle upcoming appointments
     */
    private void handleUpcomingAppointments() {
        ConsolePrinter.printSubHeader("Upcoming Appointments");
        
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointmentsByDoctor(currentDoctor.getDoctorId());
        
        if (upcomingAppointments.isEmpty()) {
            ConsolePrinter.printInfo("No upcoming appointments scheduled.");
        } else {
            appointmentService.displayAppointments(upcomingAppointments, "Upcoming Appointments");
        }
    }
    
    /**
     * Handle all appointments
     */
    private void handleAllAppointments() {
        ConsolePrinter.printSubHeader("All Appointments");
        
        List<Appointment> allAppointments = appointmentService.getAppointmentsByDoctor(currentDoctor.getDoctorId());
        
        if (allAppointments.isEmpty()) {
            ConsolePrinter.printInfo("No appointments found.");
        } else {
            appointmentService.displayAppointments(allAppointments, "All Appointments");
        }
    }
    
    /**
     * Handle available time slots
     */
    private void handleAvailableTimeSlots() {
        ConsolePrinter.printSubHeader("Available Time Slots");
        
        String dateStr = ConsolePrinter.getInput("Enter date to check (yyyy-MM-dd)", 
            input -> InputValidator.isValidDate(input));
        
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        List<String> availableSlots = appointmentService.getAvailableTimeSlots(currentDoctor.getDoctorId(), date);
        
        if (availableSlots.isEmpty()) {
            ConsolePrinter.printWarning("No available time slots for " + date);
        } else {
            ConsolePrinter.printList("Available Time Slots for " + date, availableSlots);
        }
    }
    
    /**
     * Handle patient list
     */
    private void handlePatientList() {
        ConsolePrinter.printSubHeader("Patient List");
        
        // Get patients who have appointments with this doctor
        List<Patient> patients = getPatientsWithAppointments();
        
        if (patients.isEmpty()) {
            ConsolePrinter.printWarning("No patients found.");
            return;
        }
        
        String[] headers = {"Patient ID", "Name", "Age", "Gender", "Contact", "Last Appointment"};
        List<String[]> data = new java.util.ArrayList<>();
        
        for (Patient patient : patients) {
            String lastAppointment = getLastAppointmentDate(patient.getPatientId());
            data.add(new String[]{
                patient.getPatientId(),
                patient.getFullName(),
                String.valueOf(patient.getAge()),
                patient.getGender(),
                patient.getContactNumber(),
                lastAppointment
            });
        }
        
        ConsolePrinter.printTable(headers, data);
    }
    
    /**
     * Handle medical records
     */
    private void handleMedicalRecords() {
        ConsolePrinter.printSubHeader("Medical Records");
        
        String[] menuOptions = {
            "View Patient Records",
            "Add Medical Record",
            "Update Medical Record",
            "View All Records",
            "Back to Doctor Menu"
        };
        
        ConsolePrinter.printMenu("Medical Records Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 5);
        
        switch (choice) {
            case 1:
                handleViewPatientRecords();
                break;
            case 2:
                handleAddMedicalRecord();
                break;
            case 3:
                handleUpdateMedicalRecord();
                break;
            case 4:
                handleViewAllRecords();
                break;
            case 5:
                return; // Back to doctor menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle viewing patient records
     */
    private void handleViewPatientRecords() {
        ConsolePrinter.printSubHeader("View Patient Records");
        
        String patientId = ConsolePrinter.getInput("Enter Patient ID");
        
        // Find patient
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            ConsolePrinter.printError("Patient not found.");
            return;
        }
        
        // Display patient information
        ConsolePrinter.printCard("Patient Information", 
            "Name: " + patient.getFullName() + "\n" +
            "Age: " + patient.getAge() + "\n" +
            "Gender: " + patient.getGender() + "\n" +
            "Blood Type: " + patient.getBloodType() + "\n" +
            "Contact: " + patient.getContactNumber());
        
        // Display medical records
        List<MedicalRecord> records = getMedicalRecordsByPatient(patientId);
        if (records.isEmpty()) {
            ConsolePrinter.printWarning("No medical records found for this patient.");
        } else {
            ConsolePrinter.printSubHeader("Medical Records for " + patient.getFullName());
            for (MedicalRecord record : records) {
                record.displayRecord();
                ConsolePrinter.printSeparator();
            }
        }
    }
    
    /**
     * Handle adding medical record
     */
    private void handleAddMedicalRecord() {
        ConsolePrinter.printSubHeader("Add Medical Record");
        
        String patientId = ConsolePrinter.getInput("Enter Patient ID");
        
        // Find patient
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            ConsolePrinter.printError("Patient not found.");
            return;
        }
        
        String diagnosis = ConsolePrinter.getInput("Enter Diagnosis", 
            input -> InputValidator.isValidDiagnosis(input));
        
        String prescription = ConsolePrinter.getInput("Enter Prescription", 
            input -> InputValidator.isValidPrescription(input));
        
        String notes = ConsolePrinter.getInput("Enter Notes (optional)");
        
        // Create medical record
        if (currentDoctor.addMedicalRecord(patientId, diagnosis, prescription, notes)) {
            ConsolePrinter.printSuccess("Medical record added successfully!");
        } else {
            ConsolePrinter.printError("Failed to add medical record.");
        }
    }
    
    /**
     * Handle updating medical record
     */
    private void handleUpdateMedicalRecord() {
        ConsolePrinter.printSubHeader("Update Medical Record");
        
        String recordId = ConsolePrinter.getInput("Enter Record ID");
        
        // Find record
        MedicalRecord record = findMedicalRecordById(recordId);
        if (record == null) {
            ConsolePrinter.printError("Medical record not found.");
            return;
        }
        
        String newDiagnosis = ConsolePrinter.getInput("Enter new diagnosis (or press Enter to keep current)");
        String newPrescription = ConsolePrinter.getInput("Enter new prescription (or press Enter to keep current)");
        String newTreatment = ConsolePrinter.getInput("Enter new treatment (or press Enter to keep current)");
        String newNotes = ConsolePrinter.getInput("Enter new notes (or press Enter to keep current)");
        
        if (record.updateRecord(newDiagnosis, newPrescription, newTreatment, newNotes)) {
            ConsolePrinter.printSuccess("Medical record updated successfully!");
        } else {
            ConsolePrinter.printError("Failed to update medical record.");
        }
    }
    
    /**
     * Handle viewing all records
     */
    private void handleViewAllRecords() {
        ConsolePrinter.printSubHeader("All Medical Records");
        
        List<MedicalRecord> records = getMedicalRecordsByDoctor(currentDoctor.getDoctorId());
        
        if (records.isEmpty()) {
            ConsolePrinter.printWarning("No medical records found.");
            return;
        }
        
        String[] headers = {"Record ID", "Patient ID", "Date", "Diagnosis", "Status"};
        List<String[]> data = new java.util.ArrayList<>();
        
        for (MedicalRecord record : records) {
            data.add(new String[]{
                record.getRecordId(),
                record.getPatientId(),
                record.getRecordDate().toString(),
                record.getDiagnosis(),
                record.getStatus()
            });
        }
        
        ConsolePrinter.printTable(headers, data);
    }
    
    /**
     * Handle approving/rejecting appointments
     */
    private void handleApproveRejectAppointments() {
        ConsolePrinter.printSubHeader("Approve/Reject Appointments");
        
        // Get pending appointments for this doctor
        List<Appointment> pendingAppointments = appointmentService.getPendingAppointmentsForDoctor(currentDoctor.getDoctorId());
        
        if (pendingAppointments.isEmpty()) {
            ConsolePrinter.printInfo("No pending appointments to review.");
            return;
        }
        
        // Display pending appointments
        appointmentService.displayAppointments(pendingAppointments, "Pending Appointments");
        
        String appointmentId = ConsolePrinter.getInput("Enter Appointment ID to review");
        
        // Find the appointment
        Appointment appointment = null;
        for (Appointment apt : pendingAppointments) {
            if (apt.getAppointmentId().equals(appointmentId)) {
                appointment = apt;
                break;
            }
        }
        
        if (appointment == null) {
            ConsolePrinter.printError("Appointment not found or not pending.");
            return;
        }
        
        // Display appointment details
        appointmentService.displayAppointmentDetails(appointmentId);
        
        // Ask for approval decision
        String[] options = {"Approve", "Reject", "Cancel"};
        ConsolePrinter.printMenu("What would you like to do?", options);
        int decision = ConsolePrinter.getIntInput("Enter your choice", 1, 3);
        
        switch (decision) {
            case 1: // Approve
                if (appointmentService.approveAppointment(appointmentId)) {
                    ConsolePrinter.printSuccess("Appointment approved! Patient will be notified.");
                }
                break;
            case 2: // Reject
                String reason = ConsolePrinter.getInput("Enter reason for rejection (optional)");
                if (appointmentService.rejectAppointment(appointmentId, reason)) {
                    ConsolePrinter.printSuccess("Appointment rejected. Patient will be notified.");
                }
                break;
            case 3: // Cancel
                ConsolePrinter.printInfo("Action cancelled.");
                break;
        }
    }
    
    /**
     * Handle creating prescription
     */
    private void handleCreatePrescription() {
        ConsolePrinter.printSubHeader("Create Prescription");
        
        String patientId = ConsolePrinter.getInput("Enter Patient ID");
        
        // Find patient
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            ConsolePrinter.printError("Patient not found.");
            return;
        }
        
        String instructions = ConsolePrinter.getInput("Enter general instructions for the prescription");
        String validUntilStr = ConsolePrinter.getInput("Enter valid until date (yyyy-MM-dd) or press Enter for 3 months from now");
        
        LocalDate validUntil;
        if (validUntilStr.isEmpty()) {
            validUntil = LocalDate.now().plusMonths(3);
        } else {
            try {
                validUntil = LocalDate.parse(validUntilStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                ConsolePrinter.printError("Invalid date format. Using default (3 months from now).");
                validUntil = LocalDate.now().plusMonths(3);
            }
        }
        
        int refills = ConsolePrinter.getIntInput("Enter number of refills", 0, 10);
        
        // Create prescription
        Prescription prescription = prescriptionService.createPrescription(
            patientId, currentDoctor.getDoctorId(), instructions, validUntil, refills);
        
        if (prescription != null) {
            // Add medications to the prescription
            while (ConsolePrinter.printConfirmation("Do you want to add a medication to this prescription?")) {
                String medicationName = ConsolePrinter.getInput("Enter medication name");
                String dosage = ConsolePrinter.getInput("Enter dosage");
                String frequency = ConsolePrinter.getInput("Enter frequency (e.g., 'twice daily')");
                String duration = ConsolePrinter.getInput("Enter duration (e.g., '7 days')");
                String medInstructions = ConsolePrinter.getInput("Enter special instructions (optional)");
                
                prescriptionService.addMedicationToPrescription(
                    prescription.getPrescriptionId(), medicationName, dosage, frequency, duration, medInstructions);
            }
            
            ConsolePrinter.printSuccess("Prescription created successfully!");
        }
    }
    
    /**
     * Handle viewing prescriptions
     */
    private void handleViewPrescriptions() {
        ConsolePrinter.printSubHeader("View Prescriptions");
        
        String[] menuOptions = {
            "My Prescriptions",
            "Patient Prescriptions",
            "All Prescriptions",
            "Prescription Details",
            "Back to Doctor Menu"
        };
        
        ConsolePrinter.printMenu("Prescription Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 5);
        
        switch (choice) {
            case 1:
                handleMyPrescriptions();
                break;
            case 2:
                handlePatientPrescriptions();
                break;
            case 3:
                handleAllPrescriptions();
                break;
            case 4:
                handlePrescriptionDetails();
                break;
            case 5:
                return; // Back to doctor menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle viewing doctor's prescriptions
     */
    private void handleMyPrescriptions() {
        ConsolePrinter.printSubHeader("My Prescriptions");
        
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctor(currentDoctor.getDoctorId());
        
        if (prescriptions.isEmpty()) {
            ConsolePrinter.printInfo("No prescriptions found.");
        } else {
            prescriptionService.displayPrescriptions(prescriptions, "My Prescriptions");
        }
    }
    
    /**
     * Handle viewing patient prescriptions
     */
    private void handlePatientPrescriptions() {
        ConsolePrinter.printSubHeader("Patient Prescriptions");
        
        String patientId = ConsolePrinter.getInput("Enter Patient ID");
        
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsForPatient(patientId);
        
        if (prescriptions.isEmpty()) {
            ConsolePrinter.printInfo("No prescriptions found for this patient.");
        } else {
            prescriptionService.displayPrescriptions(prescriptions, "Patient Prescriptions");
        }
    }
    
    /**
     * Handle viewing all prescriptions
     */
    private void handleAllPrescriptions() {
        ConsolePrinter.printSubHeader("All Prescriptions");
        
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        
        if (prescriptions.isEmpty()) {
            ConsolePrinter.printInfo("No prescriptions found.");
        } else {
            prescriptionService.displayPrescriptions(prescriptions, "All Prescriptions");
        }
    }
    
    /**
     * Handle viewing prescription details
     */
    private void handlePrescriptionDetails() {
        ConsolePrinter.printSubHeader("Prescription Details");
        
        String prescriptionId = ConsolePrinter.getInput("Enter Prescription ID");
        
        prescriptionService.displayPrescriptionDetails(prescriptionId);
    }
    
    /**
     * Handle viewing appointments
     */
    private void handleViewAppointments() {
        ConsolePrinter.printSubHeader("View Appointments");
        
        String[] menuOptions = {
            "Today's Appointments",
            "Upcoming Appointments",
            "All Appointments",
            "Appointment Details",
            "Back to Doctor Menu"
        };
        
        ConsolePrinter.printMenu("Appointment Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 5);
        
        switch (choice) {
            case 1:
                handleTodaysAppointments();
                break;
            case 2:
                handleUpcomingAppointments();
                break;
            case 3:
                handleAllAppointments();
                break;
            case 4:
                handleAppointmentDetails();
                break;
            case 5:
                return; // Back to doctor menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle appointment details
     */
    private void handleAppointmentDetails() {
        ConsolePrinter.printSubHeader("Appointment Details");
        
        String appointmentId = ConsolePrinter.getInput("Enter Appointment ID");
        
        appointmentService.displayAppointmentDetails(appointmentId);
        
        // Ask if doctor wants to add notes
        if (ConsolePrinter.printConfirmation("Do you want to add notes to this appointment?")) {
            String notes = ConsolePrinter.getInput("Enter notes");
            appointmentService.addAppointmentNotes(appointmentId, notes);
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
                break; // Back to doctor menu
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
     * Get patients who have appointments with this doctor
     * @return list of patients
     */
    private List<Patient> getPatientsWithAppointments() {
        // This would typically query the database for patients with appointments
        // For now, we'll return a sample list
        return new java.util.ArrayList<>();
    }
    
    /**
     * Get last appointment date for a patient
     * @param patientId patient ID
     * @return last appointment date or "Never"
     */
    private String getLastAppointmentDate(String patientId) {
        // This would typically query the database for the last appointment date
        // For now, we'll return a placeholder
        return "Never";
    }
    
    /**
     * Get medical records by patient ID
     * @param patientId patient ID
     * @return list of medical records
     */
    private List<MedicalRecord> getMedicalRecordsByPatient(String patientId) {
        // This would typically query the database for medical records
        // For now, we'll return a sample list
        return new java.util.ArrayList<>();
    }
    
    /**
     * Get medical records by doctor ID
     * @param doctorId doctor ID
     * @return list of medical records
     */
    private List<MedicalRecord> getMedicalRecordsByDoctor(String doctorId) {
        // This would typically query the database for medical records
        // For now, we'll return a sample list
        return new java.util.ArrayList<>();
    }
    
    /**
     * Find patient by ID
     * @param patientId patient ID to search for
     * @return Patient object or null if not found
     */
    private Patient findPatientById(String patientId) {
        // This would typically query the database for the patient
        // For now, we'll return null
        return null;
    }
    
    /**
     * Find medical record by ID
     * @param recordId record ID to search for
     * @return MedicalRecord object or null if not found
     */
    private MedicalRecord findMedicalRecordById(String recordId) {
        // This would typically query the database for the medical record
        // For now, we'll return null
        return null;
    }
}
