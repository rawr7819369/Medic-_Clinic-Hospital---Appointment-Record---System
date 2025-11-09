package ui;

import model.*;
import service.LoginService;
import service.RegistrationService;
import service.AccessibilityService;
import util.ConsolePrinter;
import util.InputValidator;

/**
 * LoginScreen class for handling user login and authentication.
 * Implements the login screen functionality with role-based access.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class LoginScreen {
    private final LoginService loginService;
    private final RegistrationService registrationService;
    private final AccessibilityService accessibilityService;
    
    /**
     * Constructor for LoginScreen
     * @param loginService login service instance
     * @param registrationService registration service instance
     * @param accessibilityService accessibility service instance
     */
    public LoginScreen(LoginService loginService, RegistrationService registrationService, 
                       AccessibilityService accessibilityService) {
        this.loginService = loginService;
        this.registrationService = registrationService;
        this.accessibilityService = accessibilityService;
    }
    
    /**
     * Display the login screen and handle user interaction
     * @return User object if login successful, null otherwise
     */
    public User displayLoginScreen() {
        ConsolePrinter.clearScreen();
        ConsolePrinter.printWelcome("MediConnect+", "1.0");
        
        while (true) {
            ConsolePrinter.printHeader("Login Screen");
            
            String[] menuOptions = {
                "Login",
                "Register New Patient",
                "Forgot Password",
                "Accessibility Settings",
                "Exit Application"
            };
            
            ConsolePrinter.printMenu("Please select an option", menuOptions);
            
            int choice = ConsolePrinter.getIntInput("Enter your choice", 1, 5);
            
            switch (choice) {
                case 1:
                    User user = handleLogin();
                    if (user != null) {
                        return user;
                    }
                    break;
                case 2:
                    handleRegistration();
                    break;
                case 3:
                    handleForgotPassword();
                    break;
                case 4:
                    handleAccessibilitySettings();
                    break;
                case 5:
                    if (handleExit()) {
                        return null;
                    }
                    break;
                default:
                    ConsolePrinter.printError("Invalid choice. Please try again.");
            }
            
            if (choice != 5) {
                ConsolePrinter.pause("Press Enter to continue");
            }
        }
    }
    
    /**
     * Handle user login process
     * @return User object if login successful, null otherwise
     */
    private User handleLogin() {
        ConsolePrinter.printSubHeader("User Login");
        
        String username = ConsolePrinter.getInput("Enter username", 
            input -> InputValidator.isValidUsername(input));
        
        String password = ConsolePrinter.getPasswordInput("Enter password");
        
        // Display role selection
        String[] roleOptions = {"Admin", "Doctor", "Patient"};
        ConsolePrinter.printMenu("Select your role", roleOptions);
        
        int roleChoice = ConsolePrinter.getIntInput("Select role (1-3)", 1, 3);
        String selectedRole = roleOptions[roleChoice - 1];
        
        // Authenticate user with role
        User user = loginService.authenticateWithRole(username, password, selectedRole);
        
        if (user != null) {
            ConsolePrinter.printSuccess("Login successful!");
            loginService.displayLoginInfo(user);
            return user;
        } else {
            ConsolePrinter.printError("Login failed. Please check your credentials and try again.");
            return null;
        }
    }
    
    /**
     * Handle patient registration
     */
    private void handleRegistration() {
        ConsolePrinter.printSubHeader("Patient Registration");
        ConsolePrinter.printInfo("Please fill in the following information to register as a new patient:");
        
        // Get user input
        String username = ConsolePrinter.getInput("Username", 
            input -> InputValidator.isValidUsername(input) && registrationService.isUsernameAvailable(input));
        
        String password = ConsolePrinter.getPasswordInput("Password");
        String confirmPassword = ConsolePrinter.getPasswordInput("Confirm Password");
        
        String fullName = ConsolePrinter.getInput("Full Name", 
            input -> InputValidator.isValidName(input));
        
        String email = ConsolePrinter.getInput("Email Address", 
            input -> InputValidator.isValidEmail(input));
        
        String contactNumber = ConsolePrinter.getInput("Contact Number", 
            input -> InputValidator.isValidPhone(input));
        
        String address = ConsolePrinter.getInput("Address", 
            input -> InputValidator.isNotEmpty(input));
        
        int age = ConsolePrinter.getIntInput("Age", 0, 150);
        
        String[] genderOptions = {"Male", "Female", "Other", "Prefer not to say"};
        ConsolePrinter.printMenu("Select Gender", genderOptions);
        int genderChoice = ConsolePrinter.getIntInput("Select gender (1-4)", 1, 4);
        String gender = genderOptions[genderChoice - 1];
        
        String[] bloodTypeOptions = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ConsolePrinter.printMenu("Select Blood Type", bloodTypeOptions);
        int bloodTypeChoice = ConsolePrinter.getIntInput("Select blood type (1-8)", 1, 8);
        String bloodType = bloodTypeOptions[bloodTypeChoice - 1];
        
        String emergencyContact = ConsolePrinter.getInput("Emergency Contact Number", 
            input -> InputValidator.isValidPhone(input));
        
        // Accessibility options
        ConsolePrinter.printSubHeader("Accessibility Options");
        ConsolePrinter.printConfirmation("Enable Voice Assistance?");
        boolean adjustFontSize = ConsolePrinter.printConfirmation("Adjust Font Size?");
        
        if (adjustFontSize) {
            accessibilityService.setFontSize();
        }
        
        // Register patient
        Patient patient = registrationService.registerPatient(username, password, confirmPassword,
                                                             fullName, email, contactNumber, address,
                                                             age, gender, bloodType, emergencyContact);
        
        if (patient != null) {
            ConsolePrinter.printSuccess("Registration successful! You can now login with your credentials.");
            ConsolePrinter.printInfo("Your Patient ID is: " + patient.getPatientId());
        } else {
            ConsolePrinter.printError("Registration failed. Please try again.");
        }
    }
    
    /**
     * Handle forgot password functionality
     */
    private void handleForgotPassword() {
        ConsolePrinter.printSubHeader("Forgot Password");
        
        String username = ConsolePrinter.getInput("Enter your username");
        
        if (loginService.handleForgotPassword(username)) {
            ConsolePrinter.printSuccess("Password reset instructions have been sent to your registered email.");
        } else {
            ConsolePrinter.printError("Username not found. Please check your username and try again.");
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
                break; // Back to main menu
            }
            
            accessibilityService.handleAccessibilityChoice(choice);
            ConsolePrinter.pause("Press Enter to continue");
        }
    }
    
    /**
     * Handle application exit
     * @return true if user wants to exit, false otherwise
     */
    private boolean handleExit() {
        if (ConsolePrinter.printConfirmation("Are you sure you want to exit the application?")) {
            ConsolePrinter.printGoodbye("MediConnect+");
            return true;
        }
        return false;
    }
    
    /**
     * Display login statistics (for admin users)
     */
    public void displayLoginStatistics() {
        ConsolePrinter.printSubHeader("Login Statistics");
        loginService.displayLoginStatistics();
    }
    
    /**
     * Display registration statistics (for admin users)
     */
    public void displayRegistrationStatistics() {
        ConsolePrinter.printSubHeader("Registration Statistics");
        registrationService.displayRegistrationStatistics();
    }
    
    /**
     * Get login service instance
     * @return LoginService instance
     */
    public LoginService getLoginService() {
        return loginService;
    }
    
    /**
     * Get registration service instance
     * @return RegistrationService instance
     */
    public RegistrationService getRegistrationService() {
        return registrationService;
    }
    
    /**
     * Get accessibility service instance
     * @return AccessibilityService instance
     */
    public AccessibilityService getAccessibilityService() {
        return accessibilityService;
    }
}
