package ui.swing;

import model.*;
import service.*;
import util.DataStore;
import javax.swing.*;

/**
 * Main Swing application class for MediConnect+
 * Manages the application lifecycle and navigation between different UI screens
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class MediConnectSwingApp {
    private static MediConnectSwingApp instance;
    
    private DataStore dataStore;
    private LoginService loginService;
    private RegistrationService registrationService;
    private AppointmentService appointmentService;
    private ReportService reportService;
    private AccessibilityService accessibilityService;
    
    private LoginFrame loginFrame;
    private AdminDashboardFrame adminDashboardFrame;
    private DoctorDashboardFrame doctorDashboardFrame;
    private PatientHomeFrame patientHomeFrame;
    
    private User currentUser;
    private JFrame currentFrame;
    
    private MediConnectSwingApp() {
        initializeServices();
    }
    
    /**
     * Get singleton instance
     */
    public static MediConnectSwingApp getInstance() {
        if (instance == null) {
            instance = new MediConnectSwingApp();
        }
        return instance;
    }
    
    /**
     * Initialize all services
     */
    private void initializeServices() {
        dataStore = DataStore.getInstance();
        loginService = new LoginService(dataStore);
        registrationService = new RegistrationService(dataStore);
        appointmentService = new AppointmentService(dataStore);
        reportService = new ReportService(dataStore);
        accessibilityService = AccessibilityService.getInstance();
    }
    
    /**
     * Start the Swing application
     */
    public void start() {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize UI frames
        initializeFrames();
        
        // Show login screen
        showLoginScreen();
    }
    
    /**
     * Initialize all UI frames
     */
    private void initializeFrames() {
        loginFrame = new LoginFrame(this);
        adminDashboardFrame = new AdminDashboardFrame(this);
        doctorDashboardFrame = new DoctorDashboardFrame(this);
        patientHomeFrame = new PatientHomeFrame(this);
    }
    
    /**
     * Show login screen
     */
    public void showLoginScreen() {
        hideCurrentFrame();
        currentFrame = loginFrame;
        loginFrame.setVisible(true);
        loginFrame.setLocationRelativeTo(null);
    }
    
    /**
     * Show admin dashboard
     */
    public void showAdminDashboard(Admin admin) {
        currentUser = admin;
        hideCurrentFrame();
        currentFrame = adminDashboardFrame;
        adminDashboardFrame.setUser(admin);
        adminDashboardFrame.setVisible(true);
        adminDashboardFrame.setLocationRelativeTo(null);
    }
    
    /**
     * Show doctor dashboard
     */
    public void showDoctorDashboard(Doctor doctor) {
        currentUser = doctor;
        hideCurrentFrame();
        currentFrame = doctorDashboardFrame;
        doctorDashboardFrame.setUser(doctor);
        doctorDashboardFrame.setVisible(true);
        doctorDashboardFrame.setLocationRelativeTo(null);
    }
    
    /**
     * Show patient home screen
     */
    public void showPatientHome(Patient patient) {
        currentUser = patient;
        hideCurrentFrame();
        currentFrame = patientHomeFrame;
        patientHomeFrame.setUser(patient);
        patientHomeFrame.setVisible(true);
        patientHomeFrame.setLocationRelativeTo(null);
    }
    
    /**
     * Hide current frame
     */
    private void hideCurrentFrame() {
        if (currentFrame != null) {
            currentFrame.setVisible(false);
        }
    }
    
    /**
     * Get current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get login service
     */
    public LoginService getLoginService() {
        return loginService;
    }
    
    /**
     * Get registration service
     */
    public RegistrationService getRegistrationService() {
        return registrationService;
    }
    
    /**
     * Get appointment service
     */
    public AppointmentService getAppointmentService() {
        return appointmentService;
    }
    
    /**
     * Get report service
     */
    public ReportService getReportService() {
        return reportService;
    }
    
    /**
     * Get accessibility service
     */
    public AccessibilityService getAccessibilityService() {
        return accessibilityService;
    }
    
    /**
     * Get data store
     */
    public DataStore getDataStore() {
        return dataStore;
    }
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MediConnectSwingApp app = MediConnectSwingApp.getInstance();
                app.start();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error starting application: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
