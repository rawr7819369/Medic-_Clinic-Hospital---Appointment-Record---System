package ui.swing;

import model.*;
import util.InputValidator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Registration dialog for new patient registration
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class RegistrationDialog extends JDialog {
    private MediConnectSwingApp app;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    
    private JButton registerButton;
    private JButton cancelButton;
    
    public RegistrationDialog(JFrame parent, MediConnectSwingApp app) {
        super(parent, "Patient Registration", true);
        this.app = app;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialize all components
     */
    private void initializeComponents() {
        // Essential fields only
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        fullNameField = new JTextField(20);
        
        // Buttons
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");
        
        // Style components
        styleComponents();
    }
    
    /**
     * Style all components
     */
    private void styleComponents() {
        Font bodyFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        // Style text fields
        JTextField[] textFields = {usernameField, passwordField, confirmPasswordField, fullNameField};
        
        for (JTextField field : textFields) {
            field.setFont(bodyFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        }
        
        // Style buttons
        registerButton.setBackground(new Color(46, 204, 113)); // SUCCESS_COLOR - Green for positive action
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(bodyFont);
        registerButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setFocusPainted(false);
        registerButton.setContentAreaFilled(true);
        registerButton.setOpaque(true);
        
        cancelButton.setBackground(new Color(231, 76, 60)); // ERROR_COLOR - Red for negative action
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(bodyFont);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setFocusPainted(false);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setOpaque(true);
    }
    
    /**
     * Setup the layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Patient Registration Form");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create the form panel
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(fullNameField, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(confirmPasswordField, gbc);
        
        return panel;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Handle registration process
     */
    private void handleRegistration() {
        // Get form data
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText().trim();
        
        // Validate input
        if (!validateInput(username, password, confirmPassword, fullName)) {
            return;
        }
        
        // Set default values for missing fields
        String email = username + "@mediconnect.com"; // Default email
        String contact = "0000000000"; // Default contact
        String address = "Not provided"; // Default address
        int age = 25; // Default age
        String gender = "Other"; // Default gender
        String bloodType = "O+"; // Default blood type
        String emergencyContact = "0000000000"; // Default emergency contact
        
        // Register patient
        Patient patient = app.getRegistrationService().registerPatient(
            username, password, confirmPassword, fullName, email, contact, address,
            age, gender, bloodType, emergencyContact
        );
        
        if (patient != null) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful!\nYour Patient ID is: " + patient.getPatientId() + 
                "\n\nNote: You can update your profile information after logging in.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Registration failed. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate form input
     */
    private boolean validateInput(String username, String password, String confirmPassword, String fullName) {
        
        if (username.isEmpty()) {
            showError("Please enter a username.");
            usernameField.requestFocus();
            return false;
        }
        
        if (!InputValidator.isValidUsername(username)) {
            showError("Invalid username format.");
            usernameField.requestFocus();
            return false;
        }
        
        if (!app.getRegistrationService().isUsernameAvailable(username)) {
            showError("Username already exists. Please choose another.");
            usernameField.requestFocus();
            return false;
        }
        
        if (password.isEmpty()) {
            showError("Please enter a password.");
            passwordField.requestFocus();
            return false;
        }
        
        if (!InputValidator.isValidPassword(password)) {
            showError("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.");
            passwordField.requestFocus();
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocus();
            return false;
        }
        
        if (fullName.isEmpty()) {
            showError("Please enter your full name.");
            fullNameField.requestFocus();
            return false;
        }
        
        if (!InputValidator.isValidName(fullName)) {
            showError("Invalid name format.");
            fullNameField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
