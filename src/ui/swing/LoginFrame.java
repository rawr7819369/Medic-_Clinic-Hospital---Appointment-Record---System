package ui.swing;

import model.*;
import util.InputValidator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login frame for MediConnect+ Swing application
 * Handles user authentication and registration
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class LoginFrame extends BaseFrame {
    private MediConnectSwingApp app;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JButton registerButton;
    private JButton forgotPasswordButton;
    private JButton settingsButton;
    
    public LoginFrame(MediConnectSwingApp app) {
        super("MediConnect+ - Login");
        this.app = app;
        setSize(600, 700); // Increased size to ensure all components are visible
        setMinimumSize(new Dimension(500, 650)); // Set minimum size for proper layout
    }
    
    @Override
    public String getTitle() {
        return "MediConnect+ - Login";
    }
    
    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Create main content area
        JPanel centerPanel = createCenterPanel();
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Override to remove logout button from login screen
     */
    @Override
    protected JPanel createHeaderRightPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        
        // Only show settings button, no logout button on login screen
        JButton settingsButton = createStyledButton("Settings", SECONDARY_COLOR);
        settingsButton.addActionListener(e -> showSettings());
        
        panel.add(settingsButton);
        
        return panel;
    }
    
    /**
     * Create center panel with login form
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Reduced padding for better fit
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Reduced insets for better spacing
        
        // Welcome message
        JLabel welcomeLabel = createStyledLabel("Welcome to MediConnect+", titleFont);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);
        
        JLabel subtitleLabel = createStyledLabel("Smart Medical Appointment System", bodyFont);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setForeground(TEXT_COLOR.darker());
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(subtitleLabel, gbc);
        
        // Login form card
        JPanel formCard = createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 10, 10, 10); // Reduced insets for better fit
        
        // Username field
        JLabel usernameLabel = createStyledLabel("Username:", bodyFont);
        formGbc.gridx = 0; formGbc.gridy = 0; formGbc.anchor = GridBagConstraints.WEST;
        formCard.add(usernameLabel, formGbc);
        
        usernameField = createStyledTextField("Enter your username");
        formGbc.gridx = 1; formGbc.gridy = 0; formGbc.fill = GridBagConstraints.HORIZONTAL;
        formCard.add(usernameField, formGbc);
        
        // Password field
        JLabel passwordLabel = createStyledLabel("Password:", bodyFont);
        formGbc.gridx = 0; formGbc.gridy = 1; formGbc.fill = GridBagConstraints.NONE;
        formCard.add(passwordLabel, formGbc);
        
        passwordField = createStyledPasswordField();
        formGbc.gridx = 1; formGbc.gridy = 1; formGbc.fill = GridBagConstraints.HORIZONTAL;
        formCard.add(passwordField, formGbc);
        
        // Role selection
        JLabel roleLabel = createStyledLabel("Role:", bodyFont);
        formGbc.gridx = 0; formGbc.gridy = 2; formGbc.fill = GridBagConstraints.NONE;
        formCard.add(roleLabel, formGbc);
        
        String[] roles = {"Admin", "Doctor", "Patient"};
        roleComboBox = createStyledComboBox(roles);
        formGbc.gridx = 1; formGbc.gridy = 2; formGbc.fill = GridBagConstraints.HORIZONTAL;
        formCard.add(roleComboBox, formGbc);
        
        // Login button
        loginButton = createStyledButton("Login", PRIMARY_COLOR);
        loginButton.setPreferredSize(new Dimension(120, 40));
        formGbc.gridx = 0; formGbc.gridy = 3; formGbc.gridwidth = 2; formGbc.fill = GridBagConstraints.NONE;
        formGbc.anchor = GridBagConstraints.CENTER;
        formCard.add(loginButton, formGbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(formCard, gbc);
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8)); // Reduced spacing
        buttonPanel.setOpaque(false);
        
        registerButton = createStyledButton("Register Patient", SUCCESS_COLOR);
        registerButton.setPreferredSize(new Dimension(130, 35)); // Slightly smaller for better fit
        
        forgotPasswordButton = createStyledButton("Forgot Password", WARNING_COLOR);
        forgotPasswordButton.setPreferredSize(new Dimension(130, 35));
        
        settingsButton = createStyledButton("Accessibility Settings", SECONDARY_COLOR);
        settingsButton.setPreferredSize(new Dimension(160, 35)); // Slightly smaller for better fit
        
        buttonPanel.add(registerButton);
        buttonPanel.add(forgotPasswordButton);
        buttonPanel.add(settingsButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);
        
        // Default credentials info
        JPanel infoPanel = createInfoPanel();
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(infoPanel, gbc);
        
        return panel;
    }
    
    /**
     * Create info panel with default credentials
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel infoLabel = createStyledLabel("Default Login Credentials:", headerFont);
        infoLabel.setForeground(PRIMARY_COLOR);
        
        JTextArea credentialsArea = new JTextArea();
        credentialsArea.setEditable(false);
        credentialsArea.setOpaque(false);
        credentialsArea.setFont(smallFont);
        credentialsArea.setText(
            "Admin: admin / Admin123!\n" +
            "Doctor: doctor / Doctor123!\n" +
            "Patient: patient / Patient123!"
        );
        
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(credentialsArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    @Override
    protected void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
        
        // Forgot password button action
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleForgotPassword();
            }
        });
        
        // Settings button action
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAccessibilitySettings();
            }
        });
        
        // Enter key handling
        KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
        usernameField.getInputMap().put(enterKey, "login");
        usernameField.getActionMap().put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        passwordField.getInputMap().put(enterKey, "login");
        passwordField.getActionMap().put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }
    
    /**
     * Handle login process
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        
        // Validate input
        if (username.isEmpty()) {
            showError("Please enter your username.");
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter your password.");
            passwordField.requestFocus();
            return;
        }
        
        if (!InputValidator.isValidUsername(username)) {
            showError("Invalid username format.");
            usernameField.requestFocus();
            return;
        }
        
        // Authenticate user
        User user = app.getLoginService().authenticateWithRole(username, password, role);
        
        if (user != null) {
            showSuccess("Login successful! Welcome, " + user.getFullName() + "!");
            
            // Navigate to appropriate dashboard
            if (user instanceof Admin) {
                app.showAdminDashboard((Admin) user);
            } else if (user instanceof Doctor) {
                app.showDoctorDashboard((Doctor) user);
            } else if (user instanceof Patient) {
                app.showPatientHome((Patient) user);
            }
        } else {
            showError("Login failed. Please check your credentials and try again.");
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    /**
     * Handle patient registration
     */
    private void handleRegistration() {
        RegistrationDialog dialog = new RegistrationDialog(this, app);
        dialog.setVisible(true);
    }
    
    /**
     * Handle forgot password
     */
    private void handleForgotPassword() {
        String username = JOptionPane.showInputDialog(this, 
            "Enter your username:", "Forgot Password", JOptionPane.QUESTION_MESSAGE);
        
        if (username != null && !username.trim().isEmpty()) {
            if (app.getLoginService().handleForgotPassword(username.trim())) {
                showSuccess("Password reset instructions have been sent to your registered email.");
            } else {
                showError("Username not found. Please check your username and try again.");
            }
        }
    }
    
    /**
     * Handle accessibility settings
     */
    private void handleAccessibilitySettings() {
        AccessibilitySettingsDialog dialog = new AccessibilitySettingsDialog(this, app);
        dialog.setVisible(true);
    }
    
    /**
     * Clear form fields
     */
    public void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        usernameField.requestFocus();
    }
}
