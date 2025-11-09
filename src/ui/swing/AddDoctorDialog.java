package ui.swing;

import model.*;
import util.InputValidator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Add doctor dialog for admin to add new doctors
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class AddDoctorDialog extends JDialog {
    private MediConnectSwingApp app;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField contactField;
    private JTextField addressField;
    private JTextField specializationField;
    private JTextField licenseField;
    private JTextField experienceField;
    
    private JButton addButton;
    private JButton cancelButton;
    
    public AddDoctorDialog(JFrame parent, MediConnectSwingApp app) {
        super(parent, "Add New Doctor", true);
        this.app = app;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setSize(600, 700);
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialize all components
     */
    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        fullNameField = new JTextField(20);
        emailField = new JTextField(20);
        contactField = new JTextField(20);
        addressField = new JTextField(20);
        specializationField = new JTextField(20);
        licenseField = new JTextField(20);
        experienceField = new JTextField(20);
        
        addButton = new JButton("Add Doctor");
        cancelButton = new JButton("Cancel");
        
        styleComponents();
    }
    
    /**
     * Style all components with modern styling
     */
    private void styleComponents() {
        Font bodyFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        
        // Style text fields with modern appearance
        JTextField[] textFields = {usernameField, fullNameField, emailField, contactField, addressField, specializationField, licenseField, experienceField};
        
        for (JTextField field : textFields) {
            field.setFont(bodyFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            field.setBackground(Color.WHITE);
            field.setForeground(new Color(44, 62, 80));
        }
        
        // Style password fields separately
        JPasswordField[] passwordFields = {passwordField, confirmPasswordField};
        for (JPasswordField field : passwordFields) {
            field.setFont(bodyFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            field.setBackground(Color.WHITE);
            field.setForeground(new Color(44, 62, 80));
        }
        
        // Style buttons with modern appearance and proper text visibility
        addButton.setBackground(new Color(46, 204, 113)); // SUCCESS_COLOR - Green for positive action
        addButton.setForeground(Color.WHITE);
        addButton.setFont(buttonFont);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(true);
        addButton.setOpaque(true);
        addButton.setPreferredSize(new Dimension(110, 35));
        
        // Add hover effect for Add button
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                addButton.setBackground(new Color(39, 174, 96)); // Darker green on hover
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                addButton.setBackground(new Color(46, 204, 113)); // SUCCESS_COLOR
            }
        });
        
        cancelButton.setBackground(new Color(231, 76, 60)); // ERROR_COLOR - Red for negative action
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(buttonFont);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setFocusPainted(false);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setOpaque(true);
        cancelButton.setPreferredSize(new Dimension(110, 35));
        
        // Add hover effect for Cancel button
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                cancelButton.setBackground(new Color(192, 57, 43)); // Darker red on hover
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                cancelButton.setBackground(new Color(231, 76, 60)); // ERROR_COLOR
            }
        });
    }
    
    /**
     * Setup the layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("Add New Doctor");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Button panel with better spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create the form panel with proper alignment
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        
        int row = 0;
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(new Color(52, 73, 94));
        nameLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        fullNameField.setFont(fieldFont);
        fullNameField.setPreferredSize(new Dimension(250, 30));
        panel.add(fullNameField, gbc);
        
        row++;
        
        // Username
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(new Color(52, 73, 94));
        usernameLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(usernameLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField.setFont(fieldFont);
        usernameField.setPreferredSize(new Dimension(250, 30));
        panel.add(usernameField, gbc);
        
        row++;
        
        // Email
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(new Color(52, 73, 94));
        emailLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField.setFont(fieldFont);
        emailField.setPreferredSize(new Dimension(250, 30));
        panel.add(emailField, gbc);
        
        row++;
        
        // Contact Number
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setFont(labelFont);
        contactLabel.setForeground(new Color(52, 73, 94));
        contactLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(contactLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        contactField.setFont(fieldFont);
        contactField.setPreferredSize(new Dimension(250, 30));
        panel.add(contactField, gbc);
        
        row++;
        
        // Address
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(labelFont);
        addressLabel.setForeground(new Color(52, 73, 94));
        addressLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(addressLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        addressField.setFont(fieldFont);
        addressField.setPreferredSize(new Dimension(250, 30));
        panel.add(addressField, gbc);
        
        row++;
        
        // Specialization
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel specLabel = new JLabel("Specialization:");
        specLabel.setFont(labelFont);
        specLabel.setForeground(new Color(52, 73, 94));
        specLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(specLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        specializationField.setFont(fieldFont);
        specializationField.setPreferredSize(new Dimension(250, 30));
        panel.add(specializationField, gbc);
        
        row++;
        
        // License Number
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel licenseLabel = new JLabel("License Number:");
        licenseLabel.setFont(labelFont);
        licenseLabel.setForeground(new Color(52, 73, 94));
        licenseLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(licenseLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        licenseField.setFont(fieldFont);
        licenseField.setPreferredSize(new Dimension(250, 30));
        panel.add(licenseField, gbc);
        
        row++;
        
        // Years of Experience
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel experienceLabel = new JLabel("Years of Experience:");
        experienceLabel.setFont(labelFont);
        experienceLabel.setForeground(new Color(52, 73, 94));
        experienceLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(experienceLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        experienceField.setFont(fieldFont);
        experienceField.setPreferredSize(new Dimension(250, 30));
        panel.add(experienceField, gbc);
        
        row++;
        
        // Password
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(new Color(52, 73, 94));
        passwordLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(passwordLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField.setFont(fieldFont);
        passwordField.setPreferredSize(new Dimension(250, 30));
        panel.add(passwordField, gbc);
        
        row++;
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(labelFont);
        confirmPasswordLabel.setForeground(new Color(52, 73, 94));
        confirmPasswordLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(confirmPasswordLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        confirmPasswordField.setFont(fieldFont);
        confirmPasswordField.setPreferredSize(new Dimension(250, 30));
        panel.add(confirmPasswordField, gbc);
        
        return panel;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddDoctor();
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
     * Handle add doctor process
     */
    private void handleAddDoctor() {
        // Get form data
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();
        String address = addressField.getText().trim();
        String specialization = specializationField.getText().trim();
        String licenseNumber = licenseField.getText().trim();
        String experienceStr = experienceField.getText().trim();
        
        // Validate input
        if (!validateInput(username, password, confirmPassword, fullName, email, contact, address, specialization, licenseNumber, experienceStr)) {
            return;
        }
        
        int experienceYears = Integer.parseInt(experienceStr);
        
        // Register doctor
        Doctor doctor = app.getRegistrationService().registerDoctor(
            username, password, fullName, email, contact, address,
            specialization, licenseNumber, experienceYears
        );
        
        if (doctor != null) {
            JOptionPane.showMessageDialog(this, 
                "Doctor added successfully!\nDoctor ID: " + doctor.getDoctorId(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add doctor. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate form input
     */
    private boolean validateInput(String username, String password, String confirmPassword, String fullName, 
                                 String email, String contact, String address, String specialization, 
                                 String licenseNumber, String experienceStr) {
        
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
            showError("Please enter the doctor's full name.");
            fullNameField.requestFocus();
            return false;
        }
        
        if (!InputValidator.isValidName(fullName)) {
            showError("Invalid name format.");
            fullNameField.requestFocus();
            return false;
        }
        
        if (email.isEmpty()) {
            showError("Please enter the doctor's email address.");
            emailField.requestFocus();
            return false;
        }
        
        if (!InputValidator.isValidEmail(email)) {
            showError("Invalid email format.");
            emailField.requestFocus();
            return false;
        }
        
        if (contact.isEmpty()) {
            showError("Please enter the doctor's contact number.");
            contactField.requestFocus();
            return false;
        }
        
        if (!InputValidator.isValidPhone(contact)) {
            showError("Invalid contact number format.");
            contactField.requestFocus();
            return false;
        }
        
        if (address.isEmpty()) {
            showError("Please enter the doctor's address.");
            addressField.requestFocus();
            return false;
        }
        
        if (specialization.isEmpty()) {
            showError("Please enter the doctor's specialization.");
            specializationField.requestFocus();
            return false;
        }
        
        if (licenseNumber.isEmpty()) {
            showError("Please enter the doctor's license number.");
            licenseField.requestFocus();
            return false;
        }
        
        if (experienceStr.isEmpty()) {
            showError("Please enter the doctor's years of experience.");
            experienceField.requestFocus();
            return false;
        }
        
        try {
            int experience = Integer.parseInt(experienceStr);
            if (experience < 0 || experience > 50) {
                showError("Years of experience must be between 0 and 50.");
                experienceField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid number for years of experience.");
            experienceField.requestFocus();
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
