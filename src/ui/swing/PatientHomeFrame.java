package ui.swing;

import model.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Patient home frame for MediConnect+ Swing application
 * Provides patient-specific functionality for appointment booking and medical history
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class PatientHomeFrame extends BaseFrame {
    private MediConnectSwingApp app;
    private Patient currentPatient;
    
    private JTabbedPane tabbedPane;
    private JPanel appointmentPanel;
    private JPanel medicalHistoryPanel;
    private JPanel profilePanel;
    private JPanel dashboardPanel;
    
    public PatientHomeFrame(MediConnectSwingApp app) {
        super("MediConnect+ - Patient Home");
        this.app = app;
        setSize(1200, 800);
    }
    
    /**
     * Set the current patient user
     */
    public void setUser(Patient patient) {
        this.currentPatient = patient;
        updateWelcomeMessage();
        // Refresh dashboard to reflect current patient's dynamic stats
        if (tabbedPane != null) {
            SwingUtilities.invokeLater(() -> tabbedPane.setComponentAt(0, createDashboardPanel()));
        }
    }
    
    @Override
    public String getTitle() {
        return "MediConnect+ - Patient Home";
    }
    
    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(headerFont);
        // Rebuild dashboard when selected to keep stats up-to-date
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) {
                tabbedPane.setComponentAt(0, createDashboardPanel());
            }
        });
        
        // Create tabs
        dashboardPanel = createDashboardPanel();
        appointmentPanel = createAppointmentPanel();
        medicalHistoryPanel = createMedicalHistoryPanel();
        profilePanel = createProfilePanel();
        
        tabbedPane.addTab("Dashboard", dashboardPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Medical History", medicalHistoryPanel);
        tabbedPane.addTab("Profile", profilePanel);
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create dashboard panel
     */
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Welcome section
        JPanel welcomePanel = createWelcomePanel();
        
        // Quick stats
        JPanel statsPanel = createStatsPanel();
        
        // Quick actions
        JPanel actionsPanel = createQuickActionsPanel();
        
        panel.add(welcomePanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create welcome panel
     */
    private JPanel createWelcomePanel() {
        JPanel panel = createCardPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel welcomeLabel = createStyledLabel("Welcome back, " + (currentPatient != null ? currentPatient.getFullName() : "Patient") + "!", titleFont);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(PRIMARY_COLOR);
        
        JLabel infoLabel = createStyledLabel("Manage your appointments and medical records", bodyFont);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setForeground(TEXT_COLOR);
        
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(infoLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create stats panel
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create stat cards
        panel.add(createStatCard("Upcoming Appointments", String.valueOf(getUpcomingAppointmentsCount()), PRIMARY_COLOR));
        panel.add(createStatCard("Total Appointments", String.valueOf(getTotalAppointmentsCount()), SUCCESS_COLOR));
        panel.add(createStatCard("Medical Records", String.valueOf(getMedicalRecordsCount()), SECONDARY_COLOR));
        panel.add(createStatCard("Prescriptions", String.valueOf(getPrescriptionsCount()), WARNING_COLOR));
        
        return panel;
    }
    
    /**
     * Create quick actions panel
     */
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);
        
        JButton bookAppointmentButton = createStyledButton("Book New Appointment", SUCCESS_COLOR);
        bookAppointmentButton.setPreferredSize(new Dimension(200, 50));
        bookAppointmentButton.addActionListener(e -> showBookAppointmentDialog());
        
        JButton viewAppointmentsButton = createStyledButton("View My Appointments", PRIMARY_COLOR);
        viewAppointmentsButton.setPreferredSize(new Dimension(200, 50));
        viewAppointmentsButton.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        
        JButton viewHistoryButton = createStyledButton("View Medical History", SECONDARY_COLOR);
        viewHistoryButton.setPreferredSize(new Dimension(200, 50));
        viewHistoryButton.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        
        panel.add(bookAppointmentButton);
        panel.add(viewAppointmentsButton);
        panel.add(viewHistoryButton);
        
        return panel;
    }
    
    
    /**
     * Create appointment panel
     */
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton bookAppointmentButton = createStyledButton("Book New Appointment", SUCCESS_COLOR);
        bookAppointmentButton.addActionListener(e -> showBookAppointmentDialog());
        
        JButton upcomingButton = createStyledButton("Upcoming", PRIMARY_COLOR);
        upcomingButton.addActionListener(e -> showUpcomingAppointments());
        
        JButton pastButton = createStyledButton("Past Appointments", SECONDARY_COLOR);
        pastButton.addActionListener(e -> showPastAppointments());
        
        JButton allButton = createStyledButton("All Appointments", WARNING_COLOR);
        allButton.addActionListener(e -> showAllAppointments());
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> refreshAppointments());
        
        headerPanel.add(bookAppointmentButton);
        headerPanel.add(upcomingButton);
        headerPanel.add(pastButton);
        headerPanel.add(allButton);
        headerPanel.add(refreshButton);
        
        // Appointment content
        JPanel appointmentContentPanel = createAppointmentContentPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(appointmentContentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create appointment content panel
     */
    private JPanel createAppointmentContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        // Add extra top space so header buttons are not overlapped by the table
        panel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Appointment ID", "Doctor", "Date", "Time", "Status", "Reason", "Actions"};
        Object[][] data = getAppointmentTableData();
        
        JTable appointmentTable = new JTable(data, columnNames);
        appointmentTable.setFont(bodyFont);
        appointmentTable.setRowHeight(30);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create medical history panel
     */
    private JPanel createMedicalHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton viewRecordsButton = createStyledButton("View Medical Records", PRIMARY_COLOR);
        viewRecordsButton.addActionListener(e -> showMedicalRecords());
        
        JButton viewPrescriptionsButton = createStyledButton("View Prescriptions", SECONDARY_COLOR);
        viewPrescriptionsButton.addActionListener(e -> showPrescriptions());
        
        JButton viewAllergiesButton = createStyledButton("View Allergies", WARNING_COLOR);
        viewAllergiesButton.addActionListener(e -> showAllergies());
        
        JButton viewMedicationsButton = createStyledButton("View Medications", SUCCESS_COLOR);
        viewMedicationsButton.addActionListener(e -> showMedications());
        
        JButton uploadScanButton = createStyledButton("Upload Scan", PRIMARY_COLOR);
        uploadScanButton.addActionListener(e -> showUploadScanDialog());
        
        headerPanel.add(viewRecordsButton);
        headerPanel.add(viewPrescriptionsButton);
        headerPanel.add(viewAllergiesButton);
        headerPanel.add(viewMedicationsButton);
        headerPanel.add(uploadScanButton);
        
        // Medical history content
        JPanel historyContentPanel = createMedicalHistoryContentPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(historyContentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create medical history content panel
     */
    private JPanel createMedicalHistoryContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Type", "Date", "Description", "Doctor", "Status"};
        Object[][] data = getMedicalHistoryTableData();
        
        JTable historyTable = new JTable(data, columnNames);
        historyTable.setFont(bodyFont);
        historyTable.setRowHeight(30);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        // Scans section below
        JPanel scansPanel = new JPanel(new BorderLayout());
        scansPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "Uploaded Scans"));
        String[] scansCols = {"Scan ID", "File Type", "Size (KB)", "Description", "File Path"};
        Object[][] scansData = getScansTableData();
        JTable scansTable = new JTable(scansData, scansCols);
        scansTable.setFont(bodyFont);
        scansTable.setRowHeight(28);
        JScrollPane scansScroll = new JScrollPane(scansTable);
        scansScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(scansScroll, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create profile panel
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton editProfileButton = createStyledButton("Edit Profile", PRIMARY_COLOR);
        editProfileButton.addActionListener(e -> showEditProfileDialog());
        
        JButton changePasswordButton = createStyledButton("Change Password", SECONDARY_COLOR);
        changePasswordButton.addActionListener(e -> showChangePasswordDialog());
        
        JButton addAllergyButton = createStyledButton("Add Allergy", WARNING_COLOR);
        addAllergyButton.addActionListener(e -> showAddAllergyDialog());
        
        JButton addMedicationButton = createStyledButton("Add Medication", SUCCESS_COLOR);
        addMedicationButton.addActionListener(e -> showAddMedicationDialog());
        
        JButton settingsButton = createStyledButton("Settings", PRIMARY_COLOR);
        settingsButton.addActionListener(e -> showSettingsDialog());
        
        headerPanel.add(editProfileButton);
        headerPanel.add(changePasswordButton);
        headerPanel.add(addAllergyButton);
        headerPanel.add(addMedicationButton);
        headerPanel.add(settingsButton);
        
        // Profile content
        JPanel profileContentPanel = createProfileContentPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(profileContentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create profile content panel
     */
    private JPanel createProfileContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create profile info panel
        JPanel profileInfoPanel = createProfileInfoPanel();
        
        panel.add(profileInfoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create profile info panel with proper alignment
     */
    private JPanel createProfileInfoPanel() {
        JPanel panel = createCardPanel();
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0; // Allow horizontal expansion
        
        if (currentPatient != null) {
            // Patient information with consistent field widths
            // Labels column
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
            panel.add(createStyledLabel("Patient ID:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField idField = createStyledTextField(currentPatient.getPatientId());
            idField.setEditable(false);
            idField.setPreferredSize(new Dimension(200, 30));
            panel.add(idField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Username:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField usernameField = createStyledTextField(currentPatient.getUsername());
            usernameField.setEditable(false);
            usernameField.setPreferredSize(new Dimension(200, 30));
            panel.add(usernameField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Full Name:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField nameField = createStyledTextField(currentPatient.getFullName());
            nameField.setEditable(true);
            nameField.setPreferredSize(new Dimension(200, 30));
            panel.add(nameField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Age:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField ageField = createStyledTextField(String.valueOf(currentPatient.getAge()));
            ageField.setEditable(true);
            ageField.setPreferredSize(new Dimension(200, 30));
            panel.add(ageField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Gender:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField genderField = createStyledTextField(currentPatient.getGender());
            genderField.setEditable(true);
            genderField.setPreferredSize(new Dimension(200, 30));
            panel.add(genderField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Blood Type:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField bloodTypeField = createStyledTextField(currentPatient.getBloodType());
            bloodTypeField.setEditable(true);
            bloodTypeField.setPreferredSize(new Dimension(200, 30));
            panel.add(bloodTypeField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Email:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 6; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField emailField = createStyledTextField(currentPatient.getEmail());
            emailField.setEditable(true);
            emailField.setPreferredSize(new Dimension(250, 30));
            panel.add(emailField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Contact:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 7; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField contactField = createStyledTextField(currentPatient.getContactNumber());
            contactField.setEditable(true);
            contactField.setPreferredSize(new Dimension(200, 30));
            panel.add(contactField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Address:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 8; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField addressField = createStyledTextField(currentPatient.getAddress());
            addressField.setEditable(true);
            addressField.setPreferredSize(new Dimension(300, 30));
            panel.add(addressField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 9; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
            panel.add(createStyledLabel("Emergency Contact:", bodyFont), gbc);
            gbc.gridx = 1; gbc.gridy = 9; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField emergencyContactField = createStyledTextField(currentPatient.getEmergencyContact());
            emergencyContactField.setEditable(true);
            emergencyContactField.setPreferredSize(new Dimension(200, 30));
            panel.add(emergencyContactField, gbc);
            
            // Add save button
            gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton saveButton = createStyledButton("Save Changes", SUCCESS_COLOR);
            saveButton.setPreferredSize(new Dimension(150, 35));
            saveButton.addActionListener(e -> saveProfileChanges(nameField, ageField, genderField, bloodTypeField, emailField, contactField, addressField, emergencyContactField));
            panel.add(saveButton, gbc);
        }
        
        return panel;
    }
    
    /**
     * Save profile changes
     */
    private void saveProfileChanges(JTextField nameField, JTextField ageField, JTextField genderField, 
                                   JTextField bloodTypeField, JTextField emailField, JTextField contactField, 
                                   JTextField addressField, JTextField emergencyContactField) {
        if (currentPatient != null) {
            try {
                // Update patient information
                currentPatient.setFullName(nameField.getText().trim());
                currentPatient.setAge(Integer.parseInt(ageField.getText().trim()));
                currentPatient.setGender(genderField.getText().trim());
                currentPatient.setBloodType(bloodTypeField.getText().trim());
                currentPatient.setEmail(emailField.getText().trim());
                currentPatient.setContactNumber(contactField.getText().trim());
                currentPatient.setAddress(addressField.getText().trim());
                currentPatient.setEmergencyContact(emergencyContactField.getText().trim());
                
                showSuccess("Profile updated successfully!");
            } catch (NumberFormatException e) {
                showError("Please enter a valid age (number).");
                ageField.requestFocus();
            } catch (Exception e) {
                showError("Error updating profile: " + e.getMessage());
            }
        }
    }
    
    @Override
    protected void setupEventHandlers() {
        // Event handlers are set up in individual panel creation methods
    }
    
    /**
     * Update welcome message in header
     */
    private void updateWelcomeMessage() {
        if (currentPatient != null) {
            // Update header with patient info
            JLabel welcomeLabel = new JLabel("Welcome, " + currentPatient.getFullName() + "!");
            welcomeLabel.setFont(headerFont);
            welcomeLabel.setForeground(Color.WHITE);
            // This would be added to the header panel
        }
    }
    
    // Helper methods for data retrieval
    
    private Object[][] getAppointmentTableData() {
        if (app == null || currentPatient == null) {
            return new Object[0][7];
        }
        
        List<Appointment> appointments = app.getAppointmentService().getAppointmentsByPatient(currentPatient.getPatientId());
        Object[][] data = new Object[appointments.size()][7];
        
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            data[i][0] = appointment.getAppointmentId();
            data[i][1] = getDoctorName(appointment.getDoctorId());
            data[i][2] = appointment.getDate().toString();
            data[i][3] = appointment.getTimeSlot();
            data[i][4] = appointment.getStatus();
            data[i][5] = appointment.getReason();
            data[i][6] = "View Details";
        }
        
        return data;
    }
    
    private Object[][] getMedicalHistoryTableData() {
        // This would get medical history
        Object[][] data = new Object[0][5];
        return data;
    }
    
    private Object[][] getScansTableData() {
        if (app == null || currentPatient == null) return new Object[0][5];
        List<Scan> scans = app.getDataStore().getScansByPatient(currentPatient.getPatientId());
        Object[][] data = new Object[scans.size()][5];
        for (int i = 0; i < scans.size(); i++) {
            Scan s = scans.get(i);
            data[i][0] = s.getScanId();
            data[i][1] = s.getFileType();
            data[i][2] = Math.max(1, s.getFileSizeBytes() / 1024);
            data[i][3] = s.getDescription();
            data[i][4] = s.getFilePath();
        }
        return data;
    }
    
    private String getDoctorName(String doctorId) {
        if (app == null) {
            return "Unknown";
        }
        return app.getDataStore().getAllDoctors().stream()
            .filter(d -> d.getDoctorId().equals(doctorId))
            .map(Doctor::getFullName)
            .findFirst()
            .orElse("Unknown");
    }
    
    private int getUpcomingAppointmentsCount() {
        if (app == null || currentPatient == null) return 0;
        return app.getDataStore().countUpcomingAppointmentsByPatient(currentPatient.getPatientId());
    }
    
    private int getTotalAppointmentsCount() {
        if (app == null || currentPatient == null) return 0;
        return app.getDataStore().countAppointmentsByPatient(currentPatient.getPatientId());
    }
    
    private int getMedicalRecordsCount() {
        if (app == null || currentPatient == null) return 0;
        return app.getDataStore().countMedicalRecordsByPatient(currentPatient.getPatientId());
    }
    
    private int getPrescriptionsCount() {
        if (app == null || currentPatient == null) return 0;
        return app.getDataStore().countPrescriptionsByPatient(currentPatient.getPatientId());
    }
    
    /**
     * Create a statistics card
     */
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout());
        
        JLabel titleLabel = createStyledLabel(title, bodyFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel valueLabel = createStyledLabel(value, titleFont);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    // Action methods
    private void showBookAppointmentDialog() {
        BookAppointmentDialog dialog = new BookAppointmentDialog(this, app, currentPatient);
        dialog.setVisible(true);
    }
    
    private void showUpcomingAppointments() {
        showSuccess("Showing upcoming appointments");
    }
    
    private void showPastAppointments() {
        showSuccess("Showing past appointments");
    }
    
    private void showAllAppointments() {
        showSuccess("Showing all appointments");
    }
    
    private void refreshAppointments() {
        tabbedPane.setComponentAt(1, createAppointmentPanel());
    }
    
    private void showMedicalRecords() {
        showSuccess("Showing medical records");
    }
    
    private void showPrescriptions() {
        showSuccess("Showing prescriptions");
    }
    
    private void showAllergies() {
        showSuccess("Showing allergies");
    }
    
    private void showMedications() {
        showSuccess("Showing medications");
    }
    
    private void showUploadScanDialog() {
        if (currentPatient == null) { showError("No patient context."); return; }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Scan File");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images/PDF", "jpg", "jpeg", "png", "pdf"));
        int res = chooser.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        long maxBytes = 10L * 1024 * 1024;
        String nameLower = file.getName().toLowerCase();
        if (!(nameLower.endsWith(".jpg") || nameLower.endsWith(".jpeg") || nameLower.endsWith(".png") || nameLower.endsWith(".pdf"))) {
            showError("Invalid file type. Allowed: JPG, JPEG, PNG, PDF.");
            return;
        }
        if (file.length() > maxBytes) {
            showError("File too large. Max 10MB.");
            return;
        }
        // Preview for images
        JLabel previewLabel = new JLabel("No preview available for this file.");
        if (!nameLower.endsWith(".pdf")) {
            try {
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
                    Image scaled = img.getScaledInstance(240, -1, Image.SCALE_SMOOTH);
                    previewLabel = new JLabel(new ImageIcon(scaled));
                }
            } catch (Exception ignore) {}
        }
        JTextField appointmentField = createStyledTextField("");
        JTextField descField = createStyledTextField("");
        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        form.add(createStyledLabel("Appointment ID (optional):", bodyFont)); form.add(appointmentField);
        form.add(createStyledLabel("Description:", bodyFont)); form.add(descField);
        JPanel container = new JPanel(new BorderLayout(10,10));
        container.add(form, BorderLayout.NORTH);
        container.add(new JScrollPane(previewLabel), BorderLayout.CENTER);
        int ok = JOptionPane.showConfirmDialog(this, container, "Upload Scan", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return;
        try {
            // Ensure upload dir
            Path uploadDir = new File("uploads" + File.separator + currentPatient.getPatientId()).toPath();
            Files.createDirectories(uploadDir);
            String ext = nameLower.substring(nameLower.lastIndexOf('.') + 1);
            String targetName = System.currentTimeMillis() + "_" + file.getName();
            Path target = uploadDir.resolve(targetName);
            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            // Persist scan metadata
            String scanId = app.getDataStore().generateScanId();
            Scan scan = new Scan(scanId, currentPatient.getPatientId(), appointmentField.getText().trim().isEmpty()? null : appointmentField.getText().trim(),
                                  target.toString(), ext.toUpperCase(), file.length());
            scan.setDescription(descField.getText().trim());
            boolean saved = app.getDataStore().addScan(scan);
            if (saved) {
                showSuccess("Scan uploaded successfully.");
                // Refresh history tab
                tabbedPane.setComponentAt(2, createMedicalHistoryPanel());
            } else {
                showError("Failed to save scan record.");
            }
        } catch (Exception ex) {
            showError("Upload error: " + ex.getMessage());
        }
    }
    
    private void showEditProfileDialog() {
        if (currentPatient == null) { showError("No patient context."); return; }
        JTextField emailField = createStyledTextField(currentPatient.getEmail());
        JTextField contactField = createStyledTextField(currentPatient.getContactNumber());
        JTextField addressField = createStyledTextField(currentPatient.getAddress());
        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        form.add(createStyledLabel("Email:", bodyFont)); form.add(emailField);
        form.add(createStyledLabel("Contact:", bodyFont)); form.add(contactField);
        form.add(createStyledLabel("Address:", bodyFont)); form.add(addressField);
        int res = JOptionPane.showConfirmDialog(this, form, "Edit Profile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            currentPatient.updateProfile(emailField.getText().trim(), contactField.getText().trim(), addressField.getText().trim());
            showSuccess("Profile updated.");
            // Refresh profile panel
            tabbedPane.setComponentAt(3, createProfilePanel());
        } catch (Exception ex) {
            showError("Failed to update profile: " + ex.getMessage());
        }
    }
    
    private void showChangePasswordDialog() {
        if (currentPatient == null) { showError("No patient context."); return; }
        JPasswordField currentPwd = new JPasswordField();
        JPasswordField newPwd = new JPasswordField();
        JPasswordField confirmPwd = new JPasswordField();
        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        form.add(createStyledLabel("Current Password:", bodyFont)); form.add(currentPwd);
        form.add(createStyledLabel("New Password:", bodyFont)); form.add(newPwd);
        form.add(createStyledLabel("Confirm Password:", bodyFont)); form.add(confirmPwd);
        int res = JOptionPane.showConfirmDialog(this, form, "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        String cur = new String(currentPwd.getPassword());
        String np = new String(newPwd.getPassword());
        String cp = new String(confirmPwd.getPassword());
        if (!currentPatient.validateCredentials(currentPatient.getUsername(), cur)) {
            showError("Current password is incorrect.");
            return;
        }
        if (np.length() < 6) { showError("New password must be at least 6 characters."); return; }
        if (!np.equals(cp)) { showError("Passwords do not match."); return; }
        currentPatient.setPassword(np);
        showSuccess("Password changed successfully.");
    }
    
    private void showAddAllergyDialog() {
        if (currentPatient == null) { showError("No patient context."); return; }
        String allergy = JOptionPane.showInputDialog(this, "Enter allergy name:", "Add Allergy", JOptionPane.PLAIN_MESSAGE);
        if (allergy == null) return;
        allergy = allergy.trim();
        if (allergy.isEmpty()) { showError("Allergy cannot be empty."); return; }
        currentPatient.addAllergy(allergy);
        showSuccess("Allergy added.");
    }
    
    private void showAddMedicationDialog() {
        if (currentPatient == null) { showError("No patient context."); return; }
        String med = JOptionPane.showInputDialog(this, "Enter medication name:", "Add Medication", JOptionPane.PLAIN_MESSAGE);
        if (med == null) return;
        med = med.trim();
        if (med.isEmpty()) { showError("Medication cannot be empty."); return; }
        currentPatient.addCurrentMedication(med);
        showSuccess("Medication added.");
    }

    private void showSettingsDialog() {
        AccessibilitySettingsDialog dlg = new AccessibilitySettingsDialog(this, app);
        dlg.setVisible(true);
    }
}
