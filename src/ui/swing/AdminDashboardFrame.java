package ui.swing;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Admin dashboard frame for MediConnect+ Swing application
 * Provides comprehensive admin functionality for system management
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class AdminDashboardFrame extends BaseFrame {
    private MediConnectSwingApp app;
    private Admin currentAdmin;
    
    private JTabbedPane tabbedPane;
    private JPanel doctorManagementPanel;
    private JPanel patientManagementPanel;
    private JPanel appointmentPanel;
    private JPanel reportPanel;
    private JPanel statisticsPanel;
    private JTextArea reportArea;
    
    public AdminDashboardFrame(MediConnectSwingApp app) {
        super("MediConnect+ - Admin Dashboard");
        this.app = app;
        setSize(1200, 800);
    }
    
    /**
     * Set the current admin user
     */
    public void setUser(Admin admin) {
        this.currentAdmin = admin;
        updateWelcomeMessage();
        // Refresh statistics to reflect current data
        if (tabbedPane != null) {
            SwingUtilities.invokeLater(() -> tabbedPane.setComponentAt(4, createStatisticsPanel()));
        }
    }
    
    @Override
    public String getTitle() {
        return "MediConnect+ - Admin Dashboard";
    }
    
    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(headerFont);
        // Rebuild statistics when selected to keep values up-to-date
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 4) {
                tabbedPane.setComponentAt(4, createStatisticsPanel());
            }
        });
        
        // Create tabs
        doctorManagementPanel = createDoctorManagementPanel();
        patientManagementPanel = createPatientManagementPanel();
        appointmentPanel = createAppointmentPanel();
        reportPanel = createReportPanel();
        statisticsPanel = createStatisticsPanel();
        
        tabbedPane.addTab("Doctor Management", doctorManagementPanel);
        tabbedPane.addTab("Patient Management", patientManagementPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Reports", reportPanel);
        tabbedPane.addTab("Statistics", statisticsPanel);
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create doctor management panel
     */
    private JPanel createDoctorManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header with input fields
        JPanel headerPanel = createDoctorHeaderPanel();
        
        // Doctor list
        JPanel doctorListPanel = createDoctorListPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(doctorListPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create doctor header panel with search and action buttons
     */
    private JPanel createDoctorHeaderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Search field
        JLabel searchLabel = createStyledLabel("Search Doctors:", bodyFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(searchLabel, gbc);
        
        JTextField searchField = createStyledTextField("Enter doctor name or ID...");
        searchField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(searchField, gbc);
        
        // Filter by specialization
        JLabel filterLabel = createStyledLabel("Specialization:", bodyFont);
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(filterLabel, gbc);
        
        String[] specializations = {"All", "Cardiology", "Dermatology", "Neurology", "Pediatrics", "General Medicine"};
        JComboBox<String> specializationCombo = createStyledComboBox(specializations);
        specializationCombo.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 3; gbc.gridy = 0;
        panel.add(specializationCombo, gbc);
        
        // Action buttons row
        JButton addDoctorButton = createStyledButton("Add New Doctor", SUCCESS_COLOR);
        addDoctorButton.setPreferredSize(new Dimension(140, 35));
        addDoctorButton.addActionListener(e -> showAddDoctorDialog());
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(addDoctorButton, gbc);
        
        JButton searchButton = createStyledButton("Search", PRIMARY_COLOR);
        searchButton.setPreferredSize(new Dimension(100, 35));
        searchButton.addActionListener(e -> performDoctorSearch(searchField.getText(), (String) specializationCombo.getSelectedItem()));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(searchButton, gbc);
        
        JButton refreshButton = createStyledButton("Refresh", SECONDARY_COLOR);
        refreshButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.addActionListener(e -> refreshDoctorList());
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(refreshButton, gbc);
        
        return panel;
    }
    
    /**
     * Create doctor list panel
     */
    private JPanel createDoctorListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Doctor ID", "Name", "Specialization", "Email", "Contact", "Status", "Actions"};
        Object[][] data = getDoctorTableData();
        
        JTable doctorTable = new JTable(data, columnNames);
        doctorTable.setFont(bodyFont);
        doctorTable.setRowHeight(30);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add action buttons to table
        doctorTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        doctorTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Get doctor table data
     */
    private Object[][] getDoctorTableData() {
        if (app == null) {
            // Return empty data if app is not initialized yet
            return new Object[0][7];
        }
        
        List<Doctor> doctors = new java.util.ArrayList<>(app.getDataStore().getAllDoctors());
        Object[][] data = new Object[doctors.size()][7];
        
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            data[i][0] = doctor.getDoctorId();
            data[i][1] = doctor.getFullName();
            data[i][2] = doctor.getSpecialization();
            data[i][3] = doctor.getEmail();
            data[i][4] = doctor.getContactNumber();
            data[i][5] = doctor.isActive() ? "Active" : "Inactive";
            data[i][6] = "Remove";
        }
        
        return data;
    }
    
    /**
     * Create patient management panel
     */
    private JPanel createPatientManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header with input fields
        JPanel headerPanel = createPatientHeaderPanel();
        
        // Patient list
        JPanel patientListPanel = createPatientListPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(patientListPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create patient header panel with search and action buttons
     */
    private JPanel createPatientHeaderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Search field
        JLabel searchLabel = createStyledLabel("Search Patients:", bodyFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(searchLabel, gbc);
        
        JTextField searchField = createStyledTextField("Enter patient name or ID...");
        searchField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(searchField, gbc);
        
        // Filter by gender
        JLabel filterLabel = createStyledLabel("Gender:", bodyFont);
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(filterLabel, gbc);
        
        String[] genders = {"All", "Male", "Female", "Other"};
        JComboBox<String> genderCombo = createStyledComboBox(genders);
        genderCombo.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 3; gbc.gridy = 0;
        panel.add(genderCombo, gbc);
        
        // Action buttons row
        JButton searchButton = createStyledButton("Search", PRIMARY_COLOR);
        searchButton.setPreferredSize(new Dimension(100, 35));
        searchButton.addActionListener(e -> performPatientSearch(searchField.getText(), (String) genderCombo.getSelectedItem()));
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(searchButton, gbc);
        
        JButton refreshButton = createStyledButton("Refresh", SECONDARY_COLOR);
        refreshButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.addActionListener(e -> refreshPatientList());
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(refreshButton, gbc);
        
        return panel;
    }
    
    /**
     * Create patient list panel
     */
    private JPanel createPatientListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Patient ID", "Name", "Age", "Gender", "Email", "Contact", "Status", "Actions"};
        Object[][] data = getPatientTableData();
        
        JTable patientTable = new JTable(data, columnNames);
        patientTable.setFont(bodyFont);
        patientTable.setRowHeight(30);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add action buttons to table
        patientTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        patientTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Get patient table data
     */
    private Object[][] getPatientTableData() {
        if (app == null) {
            // Return empty data if app is not initialized yet
            return new Object[0][8];
        }
        
        List<Patient> patients = new java.util.ArrayList<>(app.getDataStore().getAllPatients());
        Object[][] data = new Object[patients.size()][8];
        
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            data[i][0] = patient.getPatientId();
            data[i][1] = patient.getFullName();
            data[i][2] = patient.getAge();
            data[i][3] = patient.getGender();
            data[i][4] = patient.getEmail();
            data[i][5] = patient.getContactNumber();
            data[i][6] = patient.isActive() ? "Active" : "Inactive";
            data[i][7] = "Toggle Status";
        }
        
        return data;
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
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> refreshAppointmentList());
        
        headerPanel.add(refreshButton);
        
        // Appointment list
        JPanel appointmentListPanel = createAppointmentListPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(appointmentListPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create appointment list panel
     */
    private JPanel createAppointmentListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Appointment ID", "Patient", "Doctor", "Date", "Time", "Status", "Reason"};
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
     * Get appointment table data
     */
    private Object[][] getAppointmentTableData() {
        if (app == null) {
            // Return empty data if app is not initialized yet
            return new Object[0][7];
        }
        
        List<Appointment> appointments = app.getAppointmentService().getAllAppointments();
        Object[][] data = new Object[appointments.size()][7];
        
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            data[i][0] = appointment.getAppointmentId();
            data[i][1] = getPatientName(appointment.getPatientId());
            data[i][2] = getDoctorName(appointment.getDoctorId());
            data[i][3] = appointment.getDate().toString();
            data[i][4] = appointment.getTimeSlot();
            data[i][5] = appointment.getStatus();
            data[i][6] = appointment.getReason();
        }
        
        return data;
    }
    
    /**
     * Create report panel
     */
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton systemReportButton = createStyledButton("System Report", PRIMARY_COLOR);
        systemReportButton.addActionListener(e -> generateSystemReport());
        
        JButton appointmentReportButton = createStyledButton("Appointment Report", SECONDARY_COLOR);
        appointmentReportButton.addActionListener(e -> generateAppointmentReport());
        
        JButton exportButton = createStyledButton("Export Report", SUCCESS_COLOR);
        exportButton.addActionListener(e -> exportReport());

        JButton printButton = createStyledButton("Print Report", WARNING_COLOR);
        printButton.addActionListener(e -> printReport());
        
        headerPanel.add(systemReportButton);
        headerPanel.add(appointmentReportButton);
        headerPanel.add(exportButton);
        headerPanel.add(printButton);
        
        // Report display area
        JPanel reportDisplayPanel = createReportDisplayPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(reportDisplayPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create report display panel
     */
    private JPanel createReportDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        reportArea = new JTextArea();
        reportArea.setFont(bodyFont);
        reportArea.setEditable(false);
        reportArea.setBackground(CARD_COLOR);
        reportArea.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create statistics panel
     */
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create stat cards
        panel.add(createStatCard("Total Users", String.valueOf(getTotalUsers()), PRIMARY_COLOR));
        panel.add(createStatCard("Total Doctors", String.valueOf(getTotalDoctors()), SUCCESS_COLOR));
        panel.add(createStatCard("Total Patients", String.valueOf(getTotalPatients()), SECONDARY_COLOR));
        panel.add(createStatCard("Total Appointments", String.valueOf(getTotalAppointments()), WARNING_COLOR));
        panel.add(createStatCard("Active Appointments", String.valueOf(getActiveAppointments()), PRIMARY_COLOR));
        panel.add(createStatCard("System Uptime", "24/7", SUCCESS_COLOR));
        
        return panel;
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
    
    @Override
    protected void setupEventHandlers() {
        // Event handlers are set up in individual panel creation methods
    }
    
    /**
     * Update welcome message in header
     */
    private void updateWelcomeMessage() {
        if (currentAdmin != null) {
            // Update header with admin info
            JLabel welcomeLabel = new JLabel("Welcome, " + currentAdmin.getFullName() + "!");
            welcomeLabel.setFont(headerFont);
            welcomeLabel.setForeground(Color.WHITE);
            // This would be added to the header panel
        }
    }
    
    // Helper methods for data retrieval
    private String getPatientName(String patientId) {
        if (app == null) {
            return "Unknown";
        }
        return app.getDataStore().getAllPatients().stream()
            .filter(p -> p.getPatientId().equals(patientId))
            .map(Patient::getFullName)
            .findFirst()
            .orElse("Unknown");
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
    
    private int getTotalUsers() {
        if (app == null) {
            return 0;
        }
        return app.getDataStore().countTotalUsers();
    }
    
    private int getTotalDoctors() {
        if (app == null) {
            return 0;
        }
        return app.getDataStore().countTotalDoctors();
    }
    
    private int getTotalPatients() {
        if (app == null) {
            return 0;
        }
        return app.getDataStore().countTotalPatients();
    }
    
    private int getTotalAppointments() {
        if (app == null) {
            return 0;
        }
        return app.getDataStore().countTotalAppointments();
    }
    
    private int getActiveAppointments() {
        if (app == null) {
            return 0;
        }
        return app.getDataStore().countAppointmentsByStatus("SCHEDULED");
    }
    
    // Action methods
    private void showAddDoctorDialog() {
        AddDoctorDialog dialog = new AddDoctorDialog(this, app);
        dialog.setVisible(true);
    }
    
    /**
     * Make this public so dialogs and button editors can request a refresh
     */
    public void refreshDoctorList() {
        // Refresh doctor list
        SwingUtilities.invokeLater(() -> tabbedPane.setComponentAt(0, createDoctorManagementPanel()));
    }
    
    private void refreshPatientList() {
        // Refresh patient list
        tabbedPane.setComponentAt(1, createPatientManagementPanel());
    }
    
    private void refreshAppointmentList() {
        // Refresh appointment list
        tabbedPane.setComponentAt(2, createAppointmentPanel());
    }
    
    private void generateSystemReport() {
        if (app == null) return;
        util.DataStore ds = app.getDataStore();
        StringBuilder sb = new StringBuilder();
        sb.append("=== MediConnect+ System Report ===\n");
        sb.append(new java.util.Date()).append("\n\n");
        sb.append("Users: ").append(ds.countTotalUsers()).append("\n");
        sb.append("Doctors: ").append(ds.countTotalDoctors()).append("\n");
        sb.append("Patients: ").append(ds.countTotalPatients()).append("\n");
        sb.append("Appointments (total): ").append(ds.countTotalAppointments()).append("\n");
        sb.append("Appointments (scheduled): ").append(ds.countAppointmentsByStatus("SCHEDULED")).append("\n");
        sb.append("Appointments (cancelled): ").append(ds.countAppointmentsByStatus("CANCELLED")).append("\n\n");
        sb.append("-- Doctors --\n");
        for (model.Doctor d : ds.getAllDoctors()) {
            sb.append(d.getDoctorId()).append(" | ").append(d.getFullName()).append(" | ")
              .append(d.getSpecialization()).append("\n");
        }
        sb.append("\n-- Patients --\n");
        for (model.Patient p : ds.getAllPatients()) {
            sb.append(p.getPatientId()).append(" | ").append(p.getFullName()).append("\n");
        }
        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);
        showSuccess("System report generated.");
    }
    
    private void generateAppointmentReport() {
        if (app == null) return;
        java.util.List<model.Appointment> list = app.getAppointmentService().getAllAppointments();
        StringBuilder sb = new StringBuilder();
        sb.append("=== Appointment Report ===\n");
        sb.append(new java.util.Date()).append("\n\n");
        for (model.Appointment a : list) {
            sb.append(a.getAppointmentId()).append(" | ")
              .append(getPatientName(a.getPatientId())).append(" | ")
              .append(getDoctorName(a.getDoctorId())).append(" | ")
              .append(a.getDate()).append(" ")
              .append(a.getTimeSlot()).append(" | ")
              .append(a.getStatus()).append(" | ")
              .append(a.getReason()).append("\n");
        }
        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);
        showSuccess("Appointment report generated.");
    }
    
    private void exportReport() {
        if (reportArea == null || reportArea.getText().trim().isEmpty()) {
            showError("There is no report to export.");
            return;
        }
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setDialogTitle("Save Report As");
        chooser.setSelectedFile(new java.io.File("report.txt"));
        int res = chooser.showSaveDialog(this);
        if (res != javax.swing.JFileChooser.APPROVE_OPTION) return;
        java.io.File file = chooser.getSelectedFile();
        try (java.io.FileWriter fw = new java.io.FileWriter(file)) {
            fw.write(reportArea.getText());
            showSuccess("Report exported to " + file.getAbsolutePath());
        } catch (Exception ex) {
            showError("Failed to export report: " + ex.getMessage());
        }
    }

    private void printReport() {
        if (reportArea == null || reportArea.getText().trim().isEmpty()) {
            showError("There is no report to print.");
            return;
        }
        try {
            boolean done = reportArea.print();
            if (done) showSuccess("Report sent to printer.");
        } catch (Exception ex) {
            showError("Printing failed: " + ex.getMessage());
        }
    }
    
    /**
     * Perform doctor search functionality
     */
    private void performDoctorSearch(String searchTerm, String specialization) {
        if (searchTerm.trim().isEmpty() && "All".equals(specialization)) {
            refreshDoctorList();
            return;
        }
        
        // Filter doctors based on search criteria
        List<Doctor> allDoctors = new java.util.ArrayList<>(app.getDataStore().getAllDoctors());
        List<Doctor> filteredDoctors = allDoctors.stream()
            .filter(doctor -> {
                boolean matchesSearch = searchTerm.trim().isEmpty() || 
                    doctor.getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    doctor.getDoctorId().toLowerCase().contains(searchTerm.toLowerCase());
                
                boolean matchesSpecialization = "All".equals(specialization) || 
                    doctor.getSpecialization().equals(specialization);
                
                return matchesSearch && matchesSpecialization;
            })
            .collect(java.util.stream.Collectors.toList());
        
        // Update the table with filtered results
        updateDoctorTable(filteredDoctors);
        
        showSuccess("Search completed. Found " + filteredDoctors.size() + " doctor(s).");
    }
    
    /**
     * Perform patient search functionality
     */
    private void performPatientSearch(String searchTerm, String gender) {
        if (searchTerm.trim().isEmpty() && "All".equals(gender)) {
            refreshPatientList();
            return;
        }
        
        // Filter patients based on search criteria
        List<Patient> allPatients = new java.util.ArrayList<>(app.getDataStore().getAllPatients());
        List<Patient> filteredPatients = allPatients.stream()
            .filter(patient -> {
                boolean matchesSearch = searchTerm.trim().isEmpty() || 
                    patient.getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    patient.getPatientId().toLowerCase().contains(searchTerm.toLowerCase());
                
                boolean matchesGender = "All".equals(gender) || 
                    patient.getGender().equals(gender);
                
                return matchesSearch && matchesGender;
            })
            .collect(java.util.stream.Collectors.toList());
        
        // Update the table with filtered results
        updatePatientTable(filteredPatients);
        
        showSuccess("Search completed. Found " + filteredPatients.size() + " patient(s).");
    }
    
    /**
     * Update doctor table with filtered data
     */
    private void updateDoctorTable(List<Doctor> doctors) {
        // This would update the table with the filtered doctors
        // For now, just refresh the doctor list
        refreshDoctorList();
    }
    
    /**
     * Update patient table with filtered data
     */
    private void updatePatientTable(List<Patient> patients) {
        // This would update the table with the filtered patients
        // For now, just refresh the patient list
        refreshPatientList();
    }
}

/**
 * Custom button renderer for table cells
 */
class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected, boolean hasFocus,
                                                 int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

/**
 * Custom button editor for table cells
 */
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private int row;
    
    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            fireEditingStopped();
            handleButtonClick();
        });
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row, int column) {
        this.table = table;
        this.row = row;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }
    
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Handle button click
            System.out.println("Button clicked: " + label);
        }
        isPushed = false;
        return label;
    }
    
    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
    
    private void handleButtonClick() {
        if ("Remove".equals(label)) {
            try {
                // Get doctor ID from the table (column 0)
                Object idObj = table.getValueAt(row, 0);
                String doctorId = idObj != null ? idObj.toString() : null;
                Object nameObj = table.getValueAt(row, 1);
                String doctorName = nameObj != null ? nameObj.toString() : "this doctor";
    
                // Show confirmation dialog
                int result = JOptionPane.showConfirmDialog(table,
                    "Are you sure you want to remove doctor " + doctorName + " (ID: " + doctorId + ")?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (result == JOptionPane.YES_OPTION) {
                    // Remove doctor from data store by doctorId
                    boolean removed = false;
                    try {
                        removed = util.DataStore.getInstance().removeDoctorById(doctorId);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
    
                    if (removed) {
                        JOptionPane.showMessageDialog(table,
                            "Doctor removed successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Refresh the table on the EDT
                        refreshDoctorTable();
                    } else {
                        JOptionPane.showMessageDialog(table,
                            "Failed to remove doctor. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(table,
                    "An unexpected error occurred while trying to remove the doctor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshDoctorTable() {
        // Find AdminDashboardFrame ancestor and call its public refresh method on EDT
        SwingUtilities.invokeLater(() -> {
            Window w = SwingUtilities.getWindowAncestor(table);
            if (w instanceof AdminDashboardFrame) {
                ((AdminDashboardFrame) w).refreshDoctorList();
            } else {
                // fallback: try to use MediConnectSwingApp instance to refresh if available
                try {
                    // If the admin frame is visible, refresh it
                    // (the AdminDashboardFrame.refreshDoctorList already uses tabbedPane.setComponentAt on EDT)
                    // Not forcing anything here; primary refresh path is via ancestor frame.
                    MediConnectSwingApp.getInstance(); // Just check if instance exists
                } catch (Exception e) {
                    // ignore
                }
            }
        });
    }
}
