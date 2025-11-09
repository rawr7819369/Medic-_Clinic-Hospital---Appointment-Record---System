package ui;

import model.*;
import service.*;
import util.ConsolePrinter;
import util.InputValidator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AdminDashboard class for handling admin-specific functionality.
 * Implements admin dashboard with user management, appointment oversight, and reporting.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class AdminDashboard {
    private final LoginService loginService;
    private final RegistrationService registrationService;
    private final AppointmentService appointmentService;
    private final ReportService reportService;
    private final AccessibilityService accessibilityService;
    private Admin currentAdmin;
    
    /**
     * Constructor for AdminDashboard
     * @param loginService login service instance
     * @param registrationService registration service instance
     * @param appointmentService appointment service instance
     * @param reportService report service instance
     * @param accessibilityService accessibility service instance
     */
    public AdminDashboard(LoginService loginService, RegistrationService registrationService,
                         AppointmentService appointmentService, ReportService reportService,
                         AccessibilityService accessibilityService) {
        this.loginService = loginService;
        this.registrationService = registrationService;
        this.appointmentService = appointmentService;
        this.reportService = reportService;
        this.accessibilityService = accessibilityService;
    }
    
    /**
     * Display admin dashboard and handle admin operations
     * @param admin logged in admin user
     */
    public void displayAdminDashboard(Admin admin) {
        this.currentAdmin = admin;
        
        ConsolePrinter.clearScreen();
        ConsolePrinter.printHeader("Admin Dashboard");
        ConsolePrinter.printInfo("Welcome, " + admin.getFullName() + "!");
        ConsolePrinter.printInfo("Admin ID: " + admin.getAdminId());
        
        while (true) {
            ConsolePrinter.printSubHeader("Admin Menu");
            
            String[] menuOptions = {
                "Manage Doctors",
                "Manage Patients", 
                "View All Appointments",
                "Generate Reports",
                "System Statistics",
                "Accessibility Settings",
                "Logout"
            };
            
            ConsolePrinter.printMenu("Select an option", menuOptions);
            
            int choice = ConsolePrinter.getIntInput("Enter your choice", 1, 7);
            
            switch (choice) {
                case 1:
                    handleDoctorManagement();
                    break;
                case 2:
                    handlePatientManagement();
                    break;
                case 3:
                    handleViewAppointments();
                    break;
                case 4:
                    handleReportGeneration();
                    break;
                case 5:
                    handleSystemStatistics();
                    break;
                case 6:
                    handleAccessibilitySettings();
                    break;
                case 7:
                    if (handleLogout()) {
                        return;
                    }
                    break;
                default:
                    ConsolePrinter.printError("Invalid choice. Please try again.");
            }
            
            if (choice != 7) {
                ConsolePrinter.pause("Press Enter to continue");
            }
        }
    }
    
    /**
     * Handle doctor management operations
     */
    private void handleDoctorManagement() {
        ConsolePrinter.printSubHeader("Doctor Management");
        
        String[] menuOptions = {
            "Add New Doctor",
            "View All Doctors",
            "Remove Doctor",
            "Back to Admin Menu"
        };
        
        ConsolePrinter.printMenu("Doctor Management Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 4);
        
        switch (choice) {
            case 1:
                handleAddDoctor();
                break;
            case 2:
                handleViewDoctors();
                break;
            case 3:
                handleRemoveDoctor();
                break;
            case 4:
                return; // Back to admin menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle adding a new doctor
     */
    private void handleAddDoctor() {
        ConsolePrinter.printSubHeader("Add New Doctor");
        ConsolePrinter.printInfo("Please fill in the doctor's information:");
        
        String username = ConsolePrinter.getInput("Username", 
            input -> InputValidator.isValidUsername(input) && registrationService.isUsernameAvailable(input));
        
        String password = ConsolePrinter.getPasswordInput("Password");
        
        String fullName = ConsolePrinter.getInput("Full Name", 
            input -> InputValidator.isValidName(input));
        
        String email = ConsolePrinter.getInput("Email Address", 
            input -> InputValidator.isValidEmail(input));
        
        String contactNumber = ConsolePrinter.getInput("Contact Number", 
            input -> InputValidator.isValidPhone(input));
        
        String address = ConsolePrinter.getInput("Address", 
            input -> InputValidator.isNotEmpty(input));
        
        String specialization = ConsolePrinter.getInput("Medical Specialization", 
            input -> InputValidator.isNotEmpty(input));
        
        String licenseNumber = ConsolePrinter.getInput("Medical License Number", 
            input -> InputValidator.isNotEmpty(input));
        
        int experienceYears = ConsolePrinter.getIntInput("Years of Experience", 0, 50);
        
        Doctor doctor = registrationService.registerDoctor(username, password, fullName, email,
                                                          contactNumber, address, specialization,
                                                          licenseNumber, experienceYears);
        
        if (doctor != null) {
            ConsolePrinter.printSuccess("Doctor added successfully!");
            ConsolePrinter.printInfo("Doctor ID: " + doctor.getDoctorId());
        } else {
            ConsolePrinter.printError("Failed to add doctor. Please try again.");
        }
    }
    
    /**
     * Handle viewing all doctors
     */
    private void handleViewDoctors() {
        ConsolePrinter.printSubHeader("All Doctors");
        
        List<Doctor> doctors = new java.util.ArrayList<>(loginService.getDataStore().getAllDoctors());
        
        if (doctors.isEmpty()) {
            ConsolePrinter.printWarning("No doctors found in the system.");
            return;
        }
        
        String[] headers = {"Doctor ID", "Name", "Specialization", "Email", "Contact", "Status"};
        List<String[]> data = new java.util.ArrayList<>();
        
        for (Doctor doctor : doctors) {
            data.add(new String[]{
                doctor.getDoctorId(),
                doctor.getFullName(),
                doctor.getSpecialization(),
                doctor.getEmail(),
                doctor.getContactNumber(),
                doctor.isActive() ? "Active" : "Inactive"
            });
        }
        
        ConsolePrinter.printTable(headers, data);
    }
    
    /**
     * Handle removing a doctor
     */
    private void handleRemoveDoctor() {
        ConsolePrinter.printSubHeader("Remove Doctor");
        
        String doctorId = ConsolePrinter.getInput("Enter Doctor ID to remove");
        
        if (currentAdmin.removeDoctor(doctorId)) {
            ConsolePrinter.printSuccess("Doctor removed successfully!");
        } else {
            ConsolePrinter.printError("Failed to remove doctor. Please check the Doctor ID and try again.");
        }
    }
    
    /**
     * Handle patient management operations
     */
    private void handlePatientManagement() {
        ConsolePrinter.printSubHeader("Patient Management");
        
        String[] menuOptions = {
            "View All Patients",
            "Approve Patient Account",
            "Deactivate Patient Account",
            "Back to Admin Menu"
        };
        
        ConsolePrinter.printMenu("Patient Management Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 4);
        
        switch (choice) {
            case 1:
                handleViewPatients();
                break;
            case 2:
                handleApprovePatient();
                break;
            case 3:
                handleDeactivatePatient();
                break;
            case 4:
                return; // Back to admin menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle viewing all patients
     */
    private void handleViewPatients() {
        ConsolePrinter.printSubHeader("All Patients");
        
        List<Patient> patients = new java.util.ArrayList<>(loginService.getDataStore().getAllPatients());
        
        if (patients.isEmpty()) {
            ConsolePrinter.printWarning("No patients found in the system.");
            return;
        }
        
        String[] headers = {"Patient ID", "Name", "Age", "Gender", "Email", "Contact", "Status"};
        List<String[]> data = new java.util.ArrayList<>();
        
        for (Patient patient : patients) {
            data.add(new String[]{
                patient.getPatientId(),
                patient.getFullName(),
                String.valueOf(patient.getAge()),
                patient.getGender(),
                patient.getEmail(),
                patient.getContactNumber(),
                patient.isActive() ? "Active" : "Inactive"
            });
        }
        
        ConsolePrinter.printTable(headers, data);
    }
    
    /**
     * Handle approving patient account
     */
    private void handleApprovePatient() {
        ConsolePrinter.printSubHeader("Approve Patient Account");
        
        String patientId = ConsolePrinter.getInput("Enter Patient ID to approve");
        
        // Find patient and activate account
        Patient patient = findPatientById(patientId);
        if (patient != null) {
            patient.setActive(true);
            ConsolePrinter.printSuccess("Patient account approved successfully!");
        } else {
            ConsolePrinter.printError("Patient not found. Please check the Patient ID and try again.");
        }
    }
    
    /**
     * Handle deactivating patient account
     */
    private void handleDeactivatePatient() {
        ConsolePrinter.printSubHeader("Deactivate Patient Account");
        
        String patientId = ConsolePrinter.getInput("Enter Patient ID to deactivate");
        
        // Find patient and deactivate account
        Patient patient = findPatientById(patientId);
        if (patient != null) {
            patient.setActive(false);
            ConsolePrinter.printSuccess("Patient account deactivated successfully!");
        } else {
            ConsolePrinter.printError("Patient not found. Please check the Patient ID and try again.");
        }
    }
    
    /**
     * Handle viewing all appointments
     */
    private void handleViewAppointments() {
        ConsolePrinter.printSubHeader("All Appointments");
        
        List<Appointment> appointments = appointmentService.getAllAppointments();
        
        if (appointments.isEmpty()) {
            ConsolePrinter.printWarning("No appointments found in the system.");
            return;
        }
        
        appointmentService.displayAppointments(appointments, "All Appointments");
        
        // Show appointment statistics
        appointmentService.displayAppointmentStatistics();
    }
    
    /**
     * Handle report generation
     */
    private void handleReportGeneration() {
        ConsolePrinter.printSubHeader("Report Generation");
        
        String[] menuOptions = {
            "System Report",
            "Appointment Report (Date Range)",
            "Doctor Performance Report",
            "Patient History Report",
            "Export Report to File",
            "Back to Admin Menu"
        };
        
        ConsolePrinter.printMenu("Report Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 6);
        
        switch (choice) {
            case 1:
                handleSystemReport();
                break;
            case 2:
                handleAppointmentReport();
                break;
            case 3:
                handleDoctorPerformanceReport();
                break;
            case 4:
                handlePatientHistoryReport();
                break;
            case 5:
                handleExportReport();
                break;
            case 6:
                return; // Back to admin menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle system report generation
     */
    private void handleSystemReport() {
        ConsolePrinter.printSubHeader("System Report");
        reportService.displaySystemReport();
    }
    
    /**
     * Handle appointment report generation
     */
    private void handleAppointmentReport() {
        ConsolePrinter.printSubHeader("Appointment Report");
        
        String startDateStr = ConsolePrinter.getInput("Enter start date (yyyy-MM-dd)", 
            input -> InputValidator.isValidDate(input));
        
        String endDateStr = ConsolePrinter.getInput("Enter end date (yyyy-MM-dd)", 
            input -> InputValidator.isValidDate(input));
        
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        reportService.displayAppointmentReport(startDate, endDate);
    }
    
    /**
     * Handle doctor performance report generation
     */
    private void handleDoctorPerformanceReport() {
        ConsolePrinter.printSubHeader("Doctor Performance Report");
        
        String doctorId = ConsolePrinter.getInput("Enter Doctor ID");
        reportService.displayDoctorPerformanceReport(doctorId);
    }
    
    /**
     * Handle patient history report generation
     */
    private void handlePatientHistoryReport() {
        ConsolePrinter.printSubHeader("Patient History Report");
        
        String patientId = ConsolePrinter.getInput("Enter Patient ID");
        reportService.displayPatientHistoryReport(patientId);
    }
    
    /**
     * Handle report export
     */
    private void handleExportReport() {
        ConsolePrinter.printSubHeader("Export Report");
        
        String[] menuOptions = {
            "Export System Report",
            "Export Appointment Report",
            "Back to Report Menu"
        };
        
        ConsolePrinter.printMenu("Export Options", menuOptions);
        
        int choice = ConsolePrinter.getIntInput("Select an option", 1, 3);
        
        switch (choice) {
            case 1:
                String systemReport = reportService.generateSystemReport();
                String filename = "system_report_" + LocalDate.now() + ".txt";
                reportService.exportReportToFile(systemReport, filename);
                break;
            case 2:
                String startDateStr = ConsolePrinter.getInput("Enter start date (yyyy-MM-dd)", 
                    input -> InputValidator.isValidDate(input));
                String endDateStr = ConsolePrinter.getInput("Enter end date (yyyy-MM-dd)", 
                    input -> InputValidator.isValidDate(input));
                
                LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                
                String appointmentReport = reportService.generateAppointmentReport(startDate, endDate);
                String reportFilename = "appointment_report_" + startDate + "_to_" + endDate + ".txt";
                reportService.exportReportToFile(appointmentReport, reportFilename);
                break;
            case 3:
                return; // Back to report menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle system statistics
     */
    private void handleSystemStatistics() {
        ConsolePrinter.printSubHeader("System Statistics");
        
        // Display login statistics
        loginService.displayLoginStatistics();
        ConsolePrinter.printSeparator();
        
        // Display registration statistics
        registrationService.displayRegistrationStatistics();
        ConsolePrinter.printSeparator();
        
        // Display appointment statistics
        appointmentService.displayAppointmentStatistics();
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
                break; // Back to admin menu
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
     * Find patient by ID
     * @param patientId patient ID to search for
     * @return Patient object or null if not found
     */
    private Patient findPatientById(String patientId) {
        return loginService.getDataStore().getAllPatients().stream()
            .filter(patient -> patient.getPatientId().equals(patientId))
            .findFirst()
            .orElse(null);
    }
}
