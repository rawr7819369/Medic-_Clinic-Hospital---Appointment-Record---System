package service;

import model.*;
import util.DataStore;
import util.InputValidator;
import util.ConsolePrinter;

/**
 * RegistrationService class for handling user registration operations.
 * Implements business logic for user registration, validation, and account creation.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class RegistrationService {
    private final DataStore dataStore;
    
    /**
     * Constructor for RegistrationService
     * @param dataStore data store instance
     */
    public RegistrationService(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
    /**
     * Register a new patient
     * @param username username for the new patient
     * @param password password for the new patient
     * @param confirmPassword password confirmation
     * @param fullName full name of the patient
     * @param email email address of the patient
     * @param contactNumber contact number of the patient
     * @param address address of the patient
     * @param age age of the patient
     * @param gender gender of the patient
     * @param bloodType blood type of the patient
     * @param emergencyContact emergency contact information
     * @return Patient object if registration successful, null otherwise
     */
    public Patient registerPatient(String username, String password, String confirmPassword,
                                   String fullName, String email, String contactNumber, String address,
                                   int age, String gender, String bloodType, String emergencyContact) {
        
        // Validate all inputs
        if (!validatePatientRegistration(username, password, confirmPassword, fullName, 
                                        email, contactNumber, address, age, gender, bloodType, emergencyContact)) {
            return null;
        }
        
        // Check if username already exists
        if (dataStore.userExists(username)) {
            ConsolePrinter.printError("Username already exists. Please choose a different username.");
            return null;
        }
        
        // Generate unique patient ID
        String patientId = generatePatientId();
        
        // Create new patient
        Patient patient = new Patient(username, password, fullName, email, contactNumber, 
                                    address, patientId, age, gender, bloodType, emergencyContact);
        
        // Add patient to data store
        if (dataStore.addUser(patient)) {
            ConsolePrinter.printSuccess("Patient registration successful!");
            ConsolePrinter.printInfo("Patient ID: " + patientId);
            return patient;
        } else {
            ConsolePrinter.printError("Failed to register patient. Please try again.");
            return null;
        }
    }
    
    /**
     * Register a new doctor (admin only)
     * @param username username for the new doctor
     * @param password password for the new doctor
     * @param fullName full name of the doctor
     * @param email email address of the doctor
     * @param contactNumber contact number of the doctor
     * @param address address of the doctor
     * @param specialization medical specialization
     * @param licenseNumber medical license number
     * @param experienceYears years of experience
     * @return Doctor object if registration successful, null otherwise
     */
    public Doctor registerDoctor(String username, String password, String fullName, String email,
                                String contactNumber, String address, String specialization,
                                String licenseNumber, int experienceYears) {
        
        // Validate all inputs
        if (!validateDoctorRegistration(username, password, fullName, email, contactNumber,
                                       address, specialization, licenseNumber, experienceYears)) {
            return null;
        }
        
        // Check if username already exists
        if (dataStore.userExists(username)) {
            ConsolePrinter.printError("Username already exists. Please choose a different username.");
            return null;
        }
        
        // Generate unique doctor ID
        String doctorId = generateDoctorId();
        
        // Create new doctor
        Doctor doctor = new Doctor(username, password, fullName, email, contactNumber,
                                  address, doctorId, specialization, licenseNumber, experienceYears);
        
        // Add doctor to data store
        if (dataStore.addUser(doctor)) {
            ConsolePrinter.printSuccess("Doctor registration successful!");
            ConsolePrinter.printInfo("Doctor ID: " + doctorId);
            return doctor;
        } else {
            ConsolePrinter.printError("Failed to register doctor. Please try again.");
            return null;
        }
    }
    
    /**
     * Register a new admin (system only)
     * @param username username for the new admin
     * @param password password for the new admin
     * @param fullName full name of the admin
     * @param email email address of the admin
     * @param contactNumber contact number of the admin
     * @param address address of the admin
     * @return Admin object if registration successful, null otherwise
     */
    public Admin registerAdmin(String username, String password, String fullName, String email,
                              String contactNumber, String address) {
        
        // Validate all inputs
        if (!validateAdminRegistration(username, password, fullName, email, contactNumber, address)) {
            return null;
        }
        
        // Check if username already exists
        if (dataStore.userExists(username)) {
            ConsolePrinter.printError("Username already exists. Please choose a different username.");
            return null;
        }
        
        // Generate unique admin ID
        String adminId = generateAdminId();
        
        // Create new admin
        Admin admin = new Admin(username, password, fullName, email, contactNumber, address, adminId);
        
        // Add admin to data store
        if (dataStore.addUser(admin)) {
            ConsolePrinter.printSuccess("Admin registration successful!");
            ConsolePrinter.printInfo("Admin ID: " + adminId);
            return admin;
        } else {
            ConsolePrinter.printError("Failed to register admin. Please try again.");
            return null;
        }
    }
    
    /**
     * Validate patient registration inputs
     * @param username username to validate
     * @param password password to validate
     * @param confirmPassword password confirmation to validate
     * @param fullName full name to validate
     * @param email email to validate
     * @param contactNumber contact number to validate
     * @param address address to validate
     * @param age age to validate
     * @param gender gender to validate
     * @param bloodType blood type to validate
     * @param emergencyContact emergency contact to validate
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validatePatientRegistration(String username, String password, String confirmPassword,
                                               String fullName, String email, String contactNumber,
                                               String address, int age, String gender, String bloodType,
                                               String emergencyContact) {
        
        boolean isValid = true;
        
        // Validate username
        if (!InputValidator.isValidUsername(username)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Username", "username"));
            isValid = false;
        }
        
        // Validate password
        if (!InputValidator.isValidPassword(password)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Password", "password"));
            isValid = false;
        }
        
        // Validate password confirmation
        if (!InputValidator.passwordsMatch(password, confirmPassword)) {
            ConsolePrinter.printError("Passwords do not match");
            isValid = false;
        }
        
        // Validate full name
        if (!InputValidator.isValidName(fullName)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Full Name", "name"));
            isValid = false;
        }
        
        // Validate email
        if (!InputValidator.isValidEmail(email)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Email", "email"));
            isValid = false;
        }
        
        // Validate contact number
        if (!InputValidator.isValidPhone(contactNumber)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Contact Number", "phone"));
            isValid = false;
        }
        
        // Validate address
        if (!InputValidator.isNotEmpty(address)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Address", "notempty"));
            isValid = false;
        }
        
        // Validate age
        if (!InputValidator.isValidAge(age)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Age", "age"));
            isValid = false;
        }
        
        // Validate gender
        if (!InputValidator.isValidGender(gender)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Gender", "gender"));
            isValid = false;
        }
        
        // Validate blood type
        if (!InputValidator.isValidBloodType(bloodType)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Blood Type", "bloodtype"));
            isValid = false;
        }
        
        // Validate emergency contact
        if (!InputValidator.isValidPhone(emergencyContact)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Emergency Contact", "phone"));
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Validate doctor registration inputs
     * @param username username to validate
     * @param password password to validate
     * @param fullName full name to validate
     * @param email email to validate
     * @param contactNumber contact number to validate
     * @param address address to validate
     * @param specialization specialization to validate
     * @param licenseNumber license number to validate
     * @param experienceYears experience years to validate
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateDoctorRegistration(String username, String password, String fullName,
                                              String email, String contactNumber, String address,
                                              String specialization, String licenseNumber, int experienceYears) {
        
        boolean isValid = true;
        
        // Validate username
        if (!InputValidator.isValidUsername(username)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Username", "username"));
            isValid = false;
        }
        
        // Validate password
        if (!InputValidator.isValidPassword(password)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Password", "password"));
            isValid = false;
        }
        
        // Validate full name
        if (!InputValidator.isValidName(fullName)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Full Name", "name"));
            isValid = false;
        }
        
        // Validate email
        if (!InputValidator.isValidEmail(email)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Email", "email"));
            isValid = false;
        }
        
        // Validate contact number
        if (!InputValidator.isValidPhone(contactNumber)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Contact Number", "phone"));
            isValid = false;
        }
        
        // Validate address
        if (!InputValidator.isNotEmpty(address)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Address", "notempty"));
            isValid = false;
        }
        
        // Validate specialization
        if (!InputValidator.isNotEmpty(specialization)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Specialization", "notempty"));
            isValid = false;
        }
        
        // Validate license number
        if (!InputValidator.isNotEmpty(licenseNumber)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("License Number", "notempty"));
            isValid = false;
        }
        
        // Validate experience years
        if (experienceYears < 0 || experienceYears > 50) {
            ConsolePrinter.printError("Experience years must be between 0 and 50");
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Validate admin registration inputs
     * @param username username to validate
     * @param password password to validate
     * @param fullName full name to validate
     * @param email email to validate
     * @param contactNumber contact number to validate
     * @param address address to validate
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateAdminRegistration(String username, String password, String fullName,
                                            String email, String contactNumber, String address) {
        
        boolean isValid = true;
        
        // Validate username
        if (!InputValidator.isValidUsername(username)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Username", "username"));
            isValid = false;
        }
        
        // Validate password
        if (!InputValidator.isValidPassword(password)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Password", "password"));
            isValid = false;
        }
        
        // Validate full name
        if (!InputValidator.isValidName(fullName)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Full Name", "name"));
            isValid = false;
        }
        
        // Validate email
        if (!InputValidator.isValidEmail(email)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Email", "email"));
            isValid = false;
        }
        
        // Validate contact number
        if (!InputValidator.isValidPhone(contactNumber)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Contact Number", "phone"));
            isValid = false;
        }
        
        // Validate address
        if (!InputValidator.isNotEmpty(address)) {
            ConsolePrinter.printError(InputValidator.getValidationErrorMessage("Address", "notempty"));
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Generate unique patient ID
     * @return unique patient ID
     */
    private String generatePatientId() {
        return "PAT" + String.format("%03d", dataStore.getAllPatients().size() + 1);
    }
    
    /**
     * Generate unique doctor ID
     * @return unique doctor ID
     */
    private String generateDoctorId() {
        return "DOC" + String.format("%03d", dataStore.getAllDoctors().size() + 1);
    }
    
    /**
     * Generate unique admin ID
     * @return unique admin ID
     */
    private String generateAdminId() {
        return "ADM" + String.format("%03d", dataStore.getAllAdmins().size() + 1);
    }
    
    /**
     * Check if username is available
     * @param username username to check
     * @return true if available, false otherwise
     */
    public boolean isUsernameAvailable(String username) {
        return !dataStore.userExists(username);
    }
    
    /**
     * Get registration statistics
     * @return map containing registration statistics
     */
    public java.util.Map<String, Object> getRegistrationStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalUsers", dataStore.getAllUsers().size());
        stats.put("totalPatients", dataStore.getAllPatients().size());
        stats.put("totalDoctors", dataStore.getAllDoctors().size());
        stats.put("totalAdmins", dataStore.getAllAdmins().size());
        
        return stats;
    }
    
    /**
     * Display registration statistics
     */
    public void displayRegistrationStatistics() {
        java.util.Map<String, Object> stats = getRegistrationStatistics();
        ConsolePrinter.printSubHeader("Registration Statistics");
        ConsolePrinter.printInfo("Total Users: " + stats.get("totalUsers"));
        ConsolePrinter.printInfo("Patients: " + stats.get("totalPatients"));
        ConsolePrinter.printInfo("Doctors: " + stats.get("totalDoctors"));
        ConsolePrinter.printInfo("Admins: " + stats.get("totalAdmins"));
    }
}
