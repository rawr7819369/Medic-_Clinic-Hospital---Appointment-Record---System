// Main application class for MediConnect+ Smart Medical Appointment System

import model.*;
import service.*;
import ui.*;
import util.*;

/**
 * Main application class for MediConnect+ Smart Medical Appointment System.
 * Implements the main application entry point and navigation logic.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class Main {
    private static DataStore dataStore;
    private static LoginService loginService;
    private static RegistrationService registrationService;
    private static AppointmentService appointmentService;
    private static PrescriptionService prescriptionService;
    private static ReportService reportService;
    private static AccessibilityService accessibilityService;
    
    private static LoginScreen loginScreen;
    private static AdminDashboard adminDashboard;
    private static DoctorDashboard doctorDashboard;
    private static PatientHomeScreen patientHomeScreen;
    
    /**
     * Main method - entry point of the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Check for UI mode argument
            boolean useSwingUI = false;
            if (args.length > 0 && args[0].equalsIgnoreCase("--swing")) {
                useSwingUI = true;
            } else if (args.length > 0 && args[0].equalsIgnoreCase("--console")) {
                useSwingUI = false;
            } else {
                // Show mode selection dialog
                useSwingUI = showModeSelectionDialog();
            }
            
            if (useSwingUI) {
                // Start Swing UI
                startSwingApplication();
            } else {
                // Start Console UI
                startConsoleApplication();
            }
            
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            System.err.println("Please restart the application.");
            e.printStackTrace();
        }
    }
    
    /**
     * Show mode selection dialog
     * @return true if Swing UI should be used, false for console
     */
    private static boolean showModeSelectionDialog() {
        try {
            // Try to show a simple dialog
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            
            String[] options = {"Swing UI (Recommended)", "Console UI"};
            int choice = javax.swing.JOptionPane.showOptionDialog(null,
                "Welcome to MediConnect+!\n\nPlease select your preferred interface:",
                "MediConnect+ - Interface Selection",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
            
            return choice == 0; // 0 = Swing UI, 1 = Console UI
        } catch (Exception e) {
            // If Swing is not available, default to console
            System.out.println("Swing UI not available, starting console mode...");
            return false;
        }
    }
    
    /**
     * Start the Swing application
     */
    private static void startSwingApplication() {
        try {
            // Initialize services first
            initializeApplication();
            
            // Start Swing UI
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    ui.swing.MediConnectSwingApp app = ui.swing.MediConnectSwingApp.getInstance();
                    app.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Error starting Swing application: " + e.getMessage(), 
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            });
        } catch (Exception e) {
            System.err.println("Error initializing Swing application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Start the console application
     */
    private static void startConsoleApplication() {
        try {
            // Initialize the application
            initializeApplication();
            
            // Display welcome message
            displayWelcomeMessage();
            
            // Start the main application loop
            runApplication();
            
        } catch (Exception e) {
            ConsolePrinter.printError("An unexpected error occurred: " + e.getMessage());
            ConsolePrinter.printError("Please restart the application.");
        } finally {
            // Cleanup resources
            cleanup();
        }
    }
    
    /**
     * Initialize the application components
     */
    private static void initializeApplication() {
        ConsolePrinter.printLoading("Initializing MediConnect+ System");
        
        // Initialize data store
        dataStore = DataStore.getInstance();
        
        // Initialize services
        loginService = new LoginService(dataStore);
        registrationService = new RegistrationService(dataStore);
        appointmentService = new AppointmentService(dataStore);
        prescriptionService = new PrescriptionService(dataStore);
        reportService = new ReportService(dataStore);
        accessibilityService = AccessibilityService.getInstance();
        
        // Initialize UI screens
        loginScreen = new LoginScreen(loginService, registrationService, accessibilityService);
        adminDashboard = new AdminDashboard(loginService, registrationService, appointmentService, reportService, accessibilityService);
        doctorDashboard = new DoctorDashboard(appointmentService, prescriptionService, accessibilityService);
        patientHomeScreen = new PatientHomeScreen(appointmentService, accessibilityService);
        
        ConsolePrinter.printSuccess("Application initialized successfully!");
    }
    
    /**
     * Display welcome message and system information
     */
    private static void displayWelcomeMessage() {
        ConsolePrinter.clearScreen();
        ConsolePrinter.printHeader("MediConnect+ Smart Medical Appointment System");
        ConsolePrinter.printInfo("Version: 1.0");
        ConsolePrinter.printInfo("Language: Java");
        ConsolePrinter.printInfo("Paradigm: Object-Oriented Programming");
        ConsolePrinter.printInfo("Storage: In-Memory Collections");
        ConsolePrinter.printSeparator();
        ConsolePrinter.printInfo("Welcome to the Smart Medical Appointment Management System!");
        ConsolePrinter.printInfo("This system provides comprehensive appointment management for:");
        ConsolePrinter.printInfo("• Administrators - System management and oversight");
        ConsolePrinter.printInfo("• Doctors - Patient care and appointment management");
        ConsolePrinter.printInfo("• Patients - Appointment booking and medical history access");
        ConsolePrinter.printSeparator();
        ConsolePrinter.printInfo("The system includes accessibility features for enhanced user experience.");
        ConsolePrinter.printInfo("Please log in to continue...");
        ConsolePrinter.printSeparator();
    }
    
    /**
     * Run the main application loop
     */
    private static void runApplication() {
        while (true) {
            try {
                // Display login screen and get authenticated user
                User currentUser = loginScreen.displayLoginScreen();
                
                if (currentUser == null) {
                    // User chose to exit
                    break;
                }
                
                // Navigate to appropriate dashboard based on user role
                navigateToDashboard(currentUser);
                
            } catch (Exception e) {
                ConsolePrinter.printError("An error occurred: " + e.getMessage());
                ConsolePrinter.printError("Please try again.");
                ConsolePrinter.pause("Press Enter to continue");
            }
        }
    }
    
    /**
     * Navigate to the appropriate dashboard based on user role
     * @param user authenticated user
     */
    private static void navigateToDashboard(User user) {
        String userRole = user.getUserRole();
        
        try {
            switch (userRole) {
                case "ADMIN":
                    Admin admin = (Admin) user;
                    adminDashboard.displayAdminDashboard(admin);
                    break;
                    
                case "DOCTOR":
                    Doctor doctor = (Doctor) user;
                    doctorDashboard.displayDoctorDashboard(doctor);
                    break;
                    
                case "PATIENT":
                    Patient patient = (Patient) user;
                    patientHomeScreen.displayPatientHomeScreen(patient);
                    break;
                    
                default:
                    ConsolePrinter.printError("Unknown user role: " + userRole);
                    ConsolePrinter.printError("Please contact system administrator.");
            }
        } catch (Exception e) {
            ConsolePrinter.printError("Error in dashboard navigation: " + e.getMessage());
            ConsolePrinter.printError("Returning to login screen...");
        }
    }
    
    /**
     * Cleanup resources and display goodbye message
     */
    private static void cleanup() {
        try {
            ConsolePrinter.printGoodbye("MediConnect+");
            ConsolePrinter.printInfo("Thank you for using the Smart Medical Appointment System!");
            ConsolePrinter.printInfo("System shutdown complete.");
            
            // Close any open resources
            ConsolePrinter.close();
            
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
    
    /**
     * Display system information
     */
    public static void displaySystemInfo() {
        ConsolePrinter.printSubHeader("System Information");
        ConsolePrinter.printInfo("Application: MediConnect+ Smart Medical Appointment System");
        ConsolePrinter.printInfo("Version: 1.0");
        ConsolePrinter.printInfo("Java Version: " + System.getProperty("java.version"));
        ConsolePrinter.printInfo("Operating System: " + System.getProperty("os.name"));
        ConsolePrinter.printInfo("Architecture: " + System.getProperty("os.arch"));
        ConsolePrinter.printInfo("User Directory: " + System.getProperty("user.dir"));
    }
    
    /**
     * Display application statistics
     */
    public static void displayApplicationStatistics() {
        ConsolePrinter.printSubHeader("Application Statistics");
        
        // Display data store statistics
        ConsolePrinter.printInfo(dataStore.getDataStoreSummary());
        
        // Display accessibility settings
        ConsolePrinter.printInfo("Accessibility Settings:");
        ConsolePrinter.printInfo("Font Size: " + accessibilityService.getFontSize());
        ConsolePrinter.printInfo("Theme: " + accessibilityService.getTheme());
        ConsolePrinter.printInfo("Text-to-Speech: " + (accessibilityService.isTextToSpeechEnabled() ? "Enabled" : "Disabled"));
        ConsolePrinter.printInfo("High Contrast: " + (accessibilityService.isHighContrastEnabled() ? "Enabled" : "Disabled"));
        ConsolePrinter.printInfo("Voice Assistance: " + (accessibilityService.isVoiceAssistanceEnabled() ? "Enabled" : "Disabled"));
        ConsolePrinter.printInfo("Large Buttons: " + (accessibilityService.areLargeButtonsEnabled() ? "Enabled" : "Disabled"));
        ConsolePrinter.printInfo("Screen Reader: " + (accessibilityService.isScreenReaderEnabled() ? "Enabled" : "Disabled"));
    }
    
    /**
     * Get data store instance
     * @return DataStore instance
     */
    public static DataStore getDataStore() {
        return dataStore;
    }
    
    /**
     * Get login service instance
     * @return LoginService instance
     */
    public static LoginService getLoginService() {
        return loginService;
    }
    
    /**
     * Get registration service instance
     * @return RegistrationService instance
     */
    public static RegistrationService getRegistrationService() {
        return registrationService;
    }
    
    /**
     * Get appointment service instance
     * @return AppointmentService instance
     */
    public static AppointmentService getAppointmentService() {
        return appointmentService;
    }
    
    /**
     * Get prescription service instance
     * @return PrescriptionService instance
     */
    public static PrescriptionService getPrescriptionService() {
        return prescriptionService;
    }
    
    /**
     * Get report service instance
     * @return ReportService instance
     */
    public static ReportService getReportService() {
        return reportService;
    }
    
    /**
     * Get accessibility service instance
     * @return AccessibilityService instance
     */
    public static AccessibilityService getAccessibilityService() {
        return accessibilityService;
    }
}
