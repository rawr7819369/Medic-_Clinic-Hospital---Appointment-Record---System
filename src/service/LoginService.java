package service;

import model.*;
import util.DataStore;
import util.InputValidator;
import util.ConsolePrinter;

/**
 * LoginService class for handling user authentication and login operations.
 * Implements business logic for user login, credential validation, and role-based access.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class LoginService {
    private final DataStore dataStore;
    
    /**
     * Constructor for LoginService
     * @param dataStore data store instance
     */
    public LoginService(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
    /**
     * Authenticate user with username and password
     * @param username username to authenticate
     * @param password password to authenticate
     * @return User object if authentication successful, null otherwise
     */
    public User authenticateUser(String username, String password) {
        if (!InputValidator.isValidUsername(username)) {
            ConsolePrinter.printError("Invalid username format");
            return null;
        }
        
        if (!InputValidator.isNotEmpty(password)) {
            ConsolePrinter.printError("Password cannot be empty");
            return null;
        }
        
        if (!dataStore.validateCredentials(username, password)) {
            ConsolePrinter.printError("Invalid username or password");
            return null;
        }
        
        User user = dataStore.getUser(username);
        if (user == null || !user.isActive()) {
            ConsolePrinter.printError("User account is inactive");
            return null;
        }
        
        ConsolePrinter.printSuccess("Login successful! Welcome, " + user.getFullName());
        return user;
    }
    
    /**
     * Get user by username
     * @param username username to search for
     * @return User object or null if not found
     */
    public User getUserByUsername(String username) {
        return dataStore.getUser(username);
    }
    
    /**
     * Get user role by username
     * @param username username to search for
     * @return user role or null if not found
     */
    public String getUserRole(String username) {
        return dataStore.getUserRole(username);
    }
    
    /**
     * Check if user exists
     * @param username username to check
     * @return true if user exists, false otherwise
     */
    public boolean userExists(String username) {
        return dataStore.getUser(username) != null;
    }
    
    /**
     * Validate user credentials without authentication
     * @param username username to validate
     * @param password password to validate
     * @return true if credentials are valid, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        return dataStore.validateCredentials(username, password);
    }
    
    /**
     * Get admin user by username
     * @param username username to search for
     * @return Admin object or null if not found or not an admin
     */
    public Admin getAdminUser(String username) {
        User user = dataStore.getUser(username);
        if (user instanceof Admin) {
            return (Admin) user;
        }
        return null;
    }
    
    /**
     * Get doctor user by username
     * @param username username to search for
     * @return Doctor object or null if not found or not a doctor
     */
    public Doctor getDoctorUser(String username) {
        User user = dataStore.getUser(username);
        if (user instanceof Doctor) {
            return (Doctor) user;
        }
        return null;
    }
    
    /**
     * Get patient user by username
     * @param username username to search for
     * @return Patient object or null if not found or not a patient
     */
    public Patient getPatientUser(String username) {
        User user = dataStore.getUser(username);
        if (user instanceof Patient) {
            return (Patient) user;
        }
        return null;
    }
    
    /**
     * Check if user has specific role
     * @param username username to check
     * @param role role to check for
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(String username, String role) {
        String userRole = dataStore.getUserRole(username);
        return userRole != null && userRole.equals(role.toUpperCase());
    }
    
    /**
     * Check if user is admin
     * @param username username to check
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin(String username) {
        return hasRole(username, "ADMIN");
    }
    
    /**
     * Check if user is doctor
     * @param username username to check
     * @return true if user is doctor, false otherwise
     */
    public boolean isDoctor(String username) {
        return hasRole(username, "DOCTOR");
    }
    
    /**
     * Check if user is patient
     * @param username username to check
     * @return true if user is patient, false otherwise
     */
    public boolean isPatient(String username) {
        return hasRole(username, "PATIENT");
    }
    
    /**
     * Get user information for display
     * @param username username to get information for
     * @return formatted user information string
     */
    public String getUserInfo(String username) {
        User user = dataStore.getUser(username);
        if (user == null) {
            return "User not found";
        }
        
        return String.format("User: %s | Role: %s | Email: %s | Status: %s",
                           user.getFullName(),
                           user.getUserRole(),
                           user.getEmail(),
                           user.isActive() ? "Active" : "Inactive");
    }
    
    /**
     * Display user login information
     * @param user logged in user
     */
    public void displayLoginInfo(User user) {
        ConsolePrinter.printCard("Login Information", 
            String.format("Welcome, %s!\nRole: %s\nEmail: %s\nStatus: %s",
                         user.getFullName(),
                         user.getUserRole(),
                         user.getEmail(),
                         user.isActive() ? "Active" : "Inactive"));
    }
    
    /**
     * Handle forgot password functionality (simplified)
     * @param username username to reset password for
     * @return true if password reset initiated, false otherwise
     */
    public boolean handleForgotPassword(String username) {
        if (!userExists(username)) {
            ConsolePrinter.printError("Username not found");
            return false;
        }
        
        User user = dataStore.getUser(username);
        ConsolePrinter.printInfo("Password reset instructions have been sent to: " + user.getEmail());
        ConsolePrinter.printWarning("Note: This is a demo application. In a real system, an email would be sent.");
        return true;
    }
    
    /**
     * Validate login attempt and return appropriate user type
     * @param username username to authenticate
     * @param password password to authenticate
     * @param expectedRole expected user role
     * @return User object if authentication successful and role matches, null otherwise
     */
    public User authenticateWithRole(String username, String password, String expectedRole) {
        User user = authenticateUser(username, password);
        if (user == null) {
            return null;
        }
        
        if (!user.getUserRole().equals(expectedRole.toUpperCase())) {
            ConsolePrinter.printError("Access denied. This account does not have " + expectedRole + " privileges.");
            return null;
        }
        
        return user;
    }
    
    /**
     * Get login statistics
     * @return map containing login statistics
     */
    public java.util.Map<String, Object> getLoginStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalUsers", dataStore.getAllUsers().size());
        stats.put("activeUsers", dataStore.getAllUsers().stream()
            .mapToInt(user -> user.isActive() ? 1 : 0).sum());
        stats.put("adminCount", dataStore.getAllAdmins().size());
        stats.put("doctorCount", dataStore.getAllDoctors().size());
        stats.put("patientCount", dataStore.getAllPatients().size());
        
        return stats;
    }
    
    /**
     * Display login statistics
     */
    public void displayLoginStatistics() {
        java.util.Map<String, Object> stats = getLoginStatistics();
        ConsolePrinter.printSubHeader("System Statistics");
        ConsolePrinter.printInfo("Total Users: " + stats.get("totalUsers"));
        ConsolePrinter.printInfo("Active Users: " + stats.get("activeUsers"));
        ConsolePrinter.printInfo("Admins: " + stats.get("adminCount"));
        ConsolePrinter.printInfo("Doctors: " + stats.get("doctorCount"));
        ConsolePrinter.printInfo("Patients: " + stats.get("patientCount"));
    }
    
    /**
     * Get the data store instance
     * @return DataStore instance
     */
    public DataStore getDataStore() {
        return dataStore;
    }
}
