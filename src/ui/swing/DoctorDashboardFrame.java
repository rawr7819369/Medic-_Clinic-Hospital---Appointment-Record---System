package ui.swing;

import model.*;
import service.DatabaseService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Doctor dashboard frame for MediConnect+ Swing application
 * Provides doctor-specific functionality for patient care and appointment management
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class DoctorDashboardFrame extends BaseFrame {
    private MediConnectSwingApp app;
    private Doctor currentDoctor;
    
    private JTabbedPane tabbedPane;
    private JPanel schedulePanel;
    private JPanel patientPanel;
    private JPanel medicalRecordsPanel;
    private JPanel prescriptionPanel;
    private JPanel appointmentPanel;
    private JTable appointmentTable;
    private JComboBox<String> statusFilterCombo;
    private String dateFilterMode = "ALL"; // ALL, TODAY, UPCOMING
    
    public DoctorDashboardFrame(MediConnectSwingApp app) {
        super("MediConnect+ - Doctor Dashboard");
        this.app = app;
        setSize(1200, 800);
    }
    
    /**
     * Set the current doctor user
     */
    public void setUser(Doctor doctor) {
        this.currentDoctor = doctor;
        updateWelcomeMessage();
    }
    
    @Override
    public String getTitle() {
        return "MediConnect+ - Doctor Dashboard";
    }
    
    @Override
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(headerFont);
        
        // Create tabs
        schedulePanel = createSchedulePanel();
        patientPanel = createPatientPanel();
        medicalRecordsPanel = createMedicalRecordsPanel();
        prescriptionPanel = createPrescriptionPanel();
        appointmentPanel = createAppointmentPanel();
        
        tabbedPane.addTab("Schedule", schedulePanel);
        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Medical Records", medicalRecordsPanel);
        tabbedPane.addTab("Prescriptions", prescriptionPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create schedule panel
     */
    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton todayButton = createStyledButton("Today's Appointments", PRIMARY_COLOR);
        todayButton.addActionListener(e -> showTodaysAppointments());
        
        JButton upcomingButton = createStyledButton("Upcoming", SECONDARY_COLOR);
        upcomingButton.addActionListener(e -> showUpcomingAppointments());
        
        JButton allButton = createStyledButton("All Appointments", SUCCESS_COLOR);
        allButton.addActionListener(e -> showAllAppointments());
        
        JButton refreshButton = createStyledButton("Refresh", WARNING_COLOR);
        refreshButton.addActionListener(e -> refreshSchedule());
        
        headerPanel.add(todayButton);
        headerPanel.add(upcomingButton);
        headerPanel.add(allButton);
        headerPanel.add(refreshButton);
        
        // Schedule content
        JPanel scheduleContentPanel = createScheduleContentPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scheduleContentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create schedule content panel
     */
    private JPanel createScheduleContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Appointment ID", "Patient", "Date", "Time", "Status", "Reason", "Actions"};
        Object[][] data = getScheduleTableData();
        
        JTable scheduleTable = new JTable(data, columnNames);
        scheduleTable.setFont(bodyFont);
        scheduleTable.setRowHeight(30);
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create patient panel
     */
    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> refreshPatientList());
        
        headerPanel.add(refreshButton);
        
        // Patient list
        JPanel patientListPanel = createPatientListPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(patientListPanel, BorderLayout.CENTER);
        
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
        String[] columnNames = {"Patient ID", "Name", "Age", "Gender", "Contact", "Last Appointment", "Actions"};
        Object[][] data = getPatientTableData();
        
        JTable patientTable = new JTable(data, columnNames);
        patientTable.setFont(bodyFont);
        patientTable.setRowHeight(30);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create medical records panel
     */
    private JPanel createMedicalRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton addRecordButton = createStyledButton("Add Medical Record", SUCCESS_COLOR);
        addRecordButton.addActionListener(e -> showAddMedicalRecordDialog());
        
        JButton viewRecordButton = createStyledButton("View Records", PRIMARY_COLOR);
        viewRecordButton.addActionListener(e -> showViewRecordsDialog());
        
        JButton refreshButton = createStyledButton("Refresh", WARNING_COLOR);
        refreshButton.addActionListener(e -> refreshMedicalRecords());
        
        headerPanel.add(addRecordButton);
        headerPanel.add(viewRecordButton);
        headerPanel.add(refreshButton);
        
        // Medical records content
        JPanel recordsContentPanel = createMedicalRecordsContentPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(recordsContentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create medical records content panel
     */
    private JPanel createMedicalRecordsContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Record ID", "Patient ID", "Date", "Diagnosis", "Status"};
        Object[][] data = getMedicalRecordsTableData();
        
        JTable recordsTable = new JTable(data, columnNames);
        recordsTable.setFont(bodyFont);
        recordsTable.setRowHeight(30);
        recordsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(recordsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create prescription panel
     */
    private JPanel createPrescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton createPrescriptionButton = createStyledButton("Create Prescription", SUCCESS_COLOR);
        createPrescriptionButton.addActionListener(e -> showCreatePrescriptionDialog());
        
        JButton viewPrescriptionsButton = createStyledButton("View Prescriptions", PRIMARY_COLOR);
        viewPrescriptionsButton.addActionListener(e -> showViewPrescriptionsDialog());
        
        JButton refreshButton = createStyledButton("Refresh", WARNING_COLOR);
        refreshButton.addActionListener(e -> refreshPrescriptions());
        
        headerPanel.add(createPrescriptionButton);
        headerPanel.add(viewPrescriptionsButton);
        headerPanel.add(refreshButton);
        
        // Prescription content
        JPanel prescriptionContentPanel = createPrescriptionContentPanel();
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(prescriptionContentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create prescription content panel
     */
    private JPanel createPrescriptionContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Prescription ID", "Patient ID", "Medication", "Dosage", "Date", "Status", "Actions"};
        Object[][] data = getPrescriptionTableData();
        
        JTable prescriptionTable = new JTable(data, columnNames);
        prescriptionTable.setFont(bodyFont);
        prescriptionTable.setRowHeight(30);
        prescriptionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(prescriptionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
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
        
        // Status filter
        headerPanel.add(createStyledLabel("Filter:", bodyFont));
        statusFilterCombo = createStyledComboBox(new String[]{"All","PENDING","CONFIRMED","REJECTED","CANCELLED","COMPLETED","RESCHEDULED","SCHEDULED"});
        headerPanel.add(statusFilterCombo);

        JButton todayButton = createStyledButton("Today's Appointments", PRIMARY_COLOR);
        todayButton.addActionListener(e -> showTodaysAppointments());
        
        JButton upcomingButton = createStyledButton("Upcoming", SECONDARY_COLOR);
        upcomingButton.addActionListener(e -> showUpcomingAppointments());
        
        JButton allButton = createStyledButton("All Appointments", SUCCESS_COLOR);
        allButton.addActionListener(e -> { statusFilterCombo.setSelectedItem("All"); dateFilterMode = "ALL"; refreshAppointments(); });
        
        JButton refreshButton = createStyledButton("Refresh", WARNING_COLOR);
        refreshButton.addActionListener(e -> refreshAppointments());

        JButton approveButton = createStyledButton("Approve", SUCCESS_COLOR);
        approveButton.addActionListener(e -> approveSelectedAppointment());

        JButton denyButton = createStyledButton("Deny", WARNING_COLOR);
        denyButton.addActionListener(e -> denySelectedAppointment());

        JButton cancelButton = createStyledButton("Cancel", ERROR_COLOR);
        cancelButton.addActionListener(e -> cancelSelectedAppointment());

        JButton createButton = createStyledButton("Create Appointment", PRIMARY_COLOR);
        createButton.addActionListener(e -> openCreateAppointmentDialog());

        // Group action buttons so Create stays next to Cancel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.add(approveButton);
        actionsPanel.add(denyButton);
        actionsPanel.add(cancelButton);
        actionsPanel.add(createButton);
        
        headerPanel.add(todayButton);
        headerPanel.add(upcomingButton);
        headerPanel.add(allButton);
        headerPanel.add(refreshButton);
        headerPanel.add(actionsPanel);
        
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
    
    // Create table
    String[] columnNames = {"Appointment ID", "Patient", "Date", "Time", "Status", "Reason", "Actions"};
    Object[][] data = getAppointmentTableData();
    
    appointmentTable = new JTable(data, columnNames);
    appointmentTable.setFont(bodyFont);
    appointmentTable.setRowHeight(30);
    appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    JScrollPane scrollPane = new JScrollPane(appointmentTable);
    scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
    
    panel.add(scrollPane, BorderLayout.CENTER);
    
    return panel;
}

    @Override
    protected void setupEventHandlers() {
        // Event handlers are attached during panel creation methods
    }

/**
 * Update welcome message in header
 */
private void updateWelcomeMessage() {
    if (currentDoctor != null) {
        // Update header with doctor info
        JLabel welcomeLabel = new JLabel("Welcome, Dr. " + currentDoctor.getFullName() + "!");
        welcomeLabel.setFont(headerFont);
        welcomeLabel.setForeground(Color.WHITE);
        // This would be added to the header panel
    }
}
    
    // Helper methods for data retrieval
    private Object[][] getScheduleTableData() {
        if (app == null || currentDoctor == null) {
            return new Object[0][7];
        }
        
        List<Appointment> appointments = app.getAppointmentService().getAppointmentsByDoctor(currentDoctor.getDoctorId());
        // Apply date filter
        java.time.LocalDate today = java.time.LocalDate.now();
        if ("TODAY".equals(dateFilterMode)) {
            appointments = appointments.stream()
                .filter(a -> a.getDate().equals(today))
                .collect(java.util.stream.Collectors.toList());
        } else if ("UPCOMING".equals(dateFilterMode)) {
            appointments = appointments.stream()
                .filter(a -> a.getDate().isAfter(today))
                .collect(java.util.stream.Collectors.toList());
        }
        Object[][] data = new Object[appointments.size()][7];
        
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            data[i][0] = appointment.getAppointmentId();
            data[i][1] = getPatientName(appointment.getPatientId());
            data[i][2] = appointment.getDate().toString();
            data[i][3] = appointment.getTimeSlot();
            data[i][4] = appointment.getStatus();
            data[i][5] = appointment.getReason();
            data[i][6] = "View Details";
        }
        
        return data;
    }
    
    private Object[][] getPatientTableData() {
        if (app == null) {
            return new Object[0][7];
        }
        
        // Patients who have appointments with this doctor
        java.util.Set<String> patientIds = new java.util.HashSet<>();
        for (Appointment a : app.getAppointmentService().getAppointmentsByDoctor(currentDoctor.getDoctorId())) {
            patientIds.add(a.getPatientId());
        }
        List<Patient> patients = new java.util.ArrayList<>();
        for (Patient p : app.getDataStore().getAllPatients()) {
            if (patientIds.contains(p.getPatientId())) patients.add(p);
        }
        Object[][] data = new Object[patients.size()][7];
        
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            data[i][0] = patient.getPatientId();
            data[i][1] = patient.getFullName();
            data[i][2] = patient.getAge();
            data[i][3] = patient.getGender();
            data[i][4] = patient.getContactNumber();
            data[i][5] = "Never"; // This would be calculated
            data[i][6] = "View Details";
        }
        
        return data;
    }
    
    private Object[][] getMedicalRecordsTableData() {
        if (app == null || currentDoctor == null) return new Object[0][5];
        List<MedicalRecord> records = app.getDataStore().getMedicalRecordsByDoctor(currentDoctor.getDoctorId());
        Object[][] data = new Object[records.size()][5];
        for (int i = 0; i < records.size(); i++) {
            MedicalRecord r = records.get(i);
            data[i][0] = r.getRecordId();
            data[i][1] = r.getPatientId();
            data[i][2] = r.getRecordDate().toString();
            data[i][3] = r.getDiagnosis();
            data[i][4] = r.getStatus();
        }
        return data;
    }
    
    private Object[][] getPrescriptionTableData() {
        if (app == null || currentDoctor == null) return new Object[0][7];
        List<Prescription> list = app.getDataStore().getPrescriptionsByDoctor(currentDoctor.getDoctorId());
        // Flatten first medication name for table brevity
        Object[][] data = new Object[list.size()][7];
        for (int i = 0; i < list.size(); i++) {
            Prescription p = list.get(i);
            String medName = p.getMedications().isEmpty() ? "-" : p.getMedications().get(0).getMedicationName();
            data[i][0] = p.getPrescriptionId();
            data[i][1] = p.getPatientId();
            data[i][2] = medName;
            data[i][3] = p.getMedications().isEmpty() ? "-" : p.getMedications().get(0).getDosage();
            data[i][4] = p.getValidUntil() != null ? p.getValidUntil().toString() : "-";
            data[i][5] = p.getStatus();
            data[i][6] = "";
        }
        return data;
    }
    
    private Object[][] getAppointmentTableData() {
        if (app == null || currentDoctor == null) return new Object[0][7];
        List<Appointment> appointments = app.getAppointmentService().getAppointmentsByDoctor(currentDoctor.getDoctorId());
        String selected = statusFilterCombo != null ? (String) statusFilterCombo.getSelectedItem() : "All";
        if (selected != null && !"All".equals(selected)) {
            appointments = appointments.stream()
                .filter(a -> selected.equalsIgnoreCase(a.getStatus()))
                .collect(java.util.stream.Collectors.toList());
        }
        Object[][] data = new Object[appointments.size()][7];
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            data[i][0] = appointment.getAppointmentId();
            data[i][1] = getPatientName(appointment.getPatientId());
            data[i][2] = appointment.getDate().toString();
            data[i][3] = appointment.getTimeSlot();
            data[i][4] = appointment.getStatus();
            data[i][5] = appointment.getReason();
            data[i][6] = "";
        }
        return data;
    }

    private String getSelectedAppointmentId() {
        if (appointmentTable == null) return null;
        int row = appointmentTable.getSelectedRow();
        if (row < 0) {
            showError("Please select an appointment row first.");
            return null;
        }
        Object val = appointmentTable.getValueAt(row, 0);
        return val != null ? val.toString() : null;
    }

    private void approveSelectedAppointment() {
        String id = getSelectedAppointmentId();
        if (id == null) return;
        boolean ok = app.getAppointmentService().approveAppointment(id);
        if (ok) refreshAppointments();
    }

    private void denySelectedAppointment() {
        String id = getSelectedAppointmentId();
        if (id == null) return;
        String reason = JOptionPane.showInputDialog(this, "Reason for denial:", "Deny Appointment", JOptionPane.QUESTION_MESSAGE);
        boolean ok = app.getAppointmentService().rejectAppointment(id, reason);
        if (ok) refreshAppointments();
    }

    private void cancelSelectedAppointment() {
        String id = getSelectedAppointmentId();
        if (id == null) return;
        String[] reasons = new String[]{"Personal Emergency","Scheduling Conflict","Patient Request","Other"};
        String choice = (String) JOptionPane.showInputDialog(this, "Select a reason:", "Cancel Appointment", JOptionPane.PLAIN_MESSAGE, null, reasons, reasons[0]);
        String finalReason = choice;
        if ("Other".equals(choice)) {
            finalReason = JOptionPane.showInputDialog(this, "Enter reason:", "Cancel Appointment", JOptionPane.QUESTION_MESSAGE);
        }
        boolean ok = app.getAppointmentService().cancelAppointment(id, finalReason);
        if (ok) refreshAppointments();
    }

    private void openCreateAppointmentDialog() {
        if (currentDoctor == null) return;
        JTextField patientIdField = createStyledTextField("");
        JTextField dateField = createStyledTextField("yyyy-MM-dd");
        JTextField slotField = createStyledTextField("HH:mm-HH:mm");
        JTextField reasonField = createStyledTextField("");
        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        form.add(createStyledLabel("Patient ID:", bodyFont)); form.add(patientIdField);
        form.add(createStyledLabel("Date:", bodyFont)); form.add(dateField);
        form.add(createStyledLabel("Time Slot:", bodyFont)); form.add(slotField);
        form.add(createStyledLabel("Reason:", bodyFont)); form.add(reasonField);
        int res = JOptionPane.showConfirmDialog(this, form, "Create Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            Appointment ap = app.getAppointmentService().createAppointmentByDoctor(currentDoctor.getDoctorId(), patientIdField.getText().trim(), dateField.getText().trim(), slotField.getText().trim(), reasonField.getText().trim());
            if (ap != null) {
                showSuccess("Appointment created: " + ap.getAppointmentId());
                refreshAppointments();
            } else {
                showError("Failed to create appointment. Check inputs/time slot availability.");
            }
        }
    }
    
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
    
    // Action methods
    private void showTodaysAppointments() {
        dateFilterMode = "TODAY";
        refreshAppointments();
    }
    
    private void showUpcomingAppointments() {
        dateFilterMode = "UPCOMING";
        refreshAppointments();
    }
    
    private void showAllAppointments() {
        dateFilterMode = "ALL";
        refreshAppointments();
    }
    
    private void refreshSchedule() {
        // Refresh schedule
        tabbedPane.setComponentAt(0, createSchedulePanel());
    }
    
    private void refreshPatientList() {
        // Refresh patient list
        tabbedPane.setComponentAt(1, createPatientPanel());
    }
    
    private void refreshMedicalRecords() {
        // Refresh medical records
        tabbedPane.setComponentAt(2, createMedicalRecordsPanel());
    }
    
    private void refreshPrescriptions() {
        // Refresh prescriptions
        tabbedPane.setComponentAt(3, createPrescriptionPanel());
    }
    
    private void refreshAppointments() {
        // Refresh appointments
        tabbedPane.setComponentAt(4, createAppointmentPanel());
    }
    
    private void showAddMedicalRecordDialog() {
        if (currentDoctor == null) return;
        JTextField patientIdField = createStyledTextField("");
        JTextField diagnosisField = createStyledTextField("");
        JTextField prescriptionField = createStyledTextField("");
        JTextField treatmentField = createStyledTextField("");
        JTextField notesField = createStyledTextField("");
        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        form.add(createStyledLabel("Patient ID:", bodyFont)); form.add(patientIdField);
        form.add(createStyledLabel("Diagnosis:", bodyFont)); form.add(diagnosisField);
        form.add(createStyledLabel("Prescription:", bodyFont)); form.add(prescriptionField);
        form.add(createStyledLabel("Treatment:", bodyFont)); form.add(treatmentField);
        form.add(createStyledLabel("Notes:", bodyFont)); form.add(notesField);
        int res = JOptionPane.showConfirmDialog(this, form, "Add Medical Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            String recId = app.getDataStore().generateMedicalRecordId();
            MedicalRecord record = new MedicalRecord(recId, patientIdField.getText().trim(), currentDoctor.getDoctorId(), diagnosisField.getText().trim(), prescriptionField.getText().trim());
            record.setTreatment(treatmentField.getText().trim());
            record.setNotes(notesField.getText().trim());
            record.createRecord();
            // Persist
            try { new DatabaseService().saveMedicalRecord(record); } catch (Exception ignore) {}
            app.getDataStore().addMedicalRecord(record);
            showSuccess("Medical record added: " + recId);
            refreshMedicalRecords();
        }
    }
    
    private void showViewRecordsDialog() {
        if (app == null || currentDoctor == null) {
            showError("No doctor context available.");
            return;
        }

        JDialog dialog = new JDialog(this, "Medical Records", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(900, 500);

        // Header with refresh and close
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JButton refreshBtn = createStyledButton("Refresh", WARNING_COLOR);
        JButton viewBtn = createStyledButton("View", PRIMARY_COLOR);
        JButton closeBtn = createStyledButton("Close", ERROR_COLOR);
        header.add(refreshBtn);
        header.add(viewBtn);
        header.add(closeBtn);
        dialog.add(header, BorderLayout.NORTH);

        // Table of records
        String[] cols = {"Record ID","Patient","Date","Diagnosis","Status"};
        Object[][] data = buildDoctorRecordsTableData();
        JTable table = new JTable(data, cols);
        table.setRowHeight(28);
        table.setFont(bodyFont);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        dialog.add(sp, BorderLayout.CENTER);

        // Actions
        Runnable refresh = () -> {
            Object[][] rows = buildDoctorRecordsTableData();
            table.setModel(new javax.swing.table.DefaultTableModel(rows, cols));
        };
        refreshBtn.addActionListener(e -> refresh.run());

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { showError("Please select a record first."); return; }
            String recId = table.getValueAt(row, 0).toString();
            MedicalRecord rec = app.getDataStore().getMedicalRecord(recId);
            if (rec == null) { showError("Record not found."); return; }
            showRecordDetails(rec);
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String recId = table.getValueAt(row, 0).toString();
                        MedicalRecord rec = app.getDataStore().getMedicalRecord(recId);
                        if (rec != null) showRecordDetails(rec);
                    }
                }
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private Object[][] buildDoctorRecordsTableData() {
        List<MedicalRecord> records = app.getDataStore().getMedicalRecordsByDoctor(currentDoctor.getDoctorId());
        Object[][] data = new Object[records.size()][5];
        for (int i = 0; i < records.size(); i++) {
            MedicalRecord r = records.get(i);
            data[i][0] = r.getRecordId();
            data[i][1] = getPatientName(r.getPatientId());
            data[i][2] = r.getRecordDate().toString();
            data[i][3] = r.getDiagnosis();
            data[i][4] = r.getStatus();
        }
        return data;
    }

    private void showRecordDetails(MedicalRecord r) {
        JPanel panel = new JPanel(new GridLayout(0,2,8,8));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(createStyledLabel("Record ID:", bodyFont)); panel.add(createStyledLabel(r.getRecordId(), bodyFont));
        panel.add(createStyledLabel("Patient:", bodyFont)); panel.add(createStyledLabel(getPatientName(r.getPatientId()), bodyFont));
        panel.add(createStyledLabel("Date:", bodyFont)); panel.add(createStyledLabel(r.getRecordDate().toString(), bodyFont));
        panel.add(createStyledLabel("Diagnosis:", bodyFont)); panel.add(createStyledLabel(nullToDash(r.getDiagnosis()), bodyFont));
        panel.add(createStyledLabel("Prescription:", bodyFont)); panel.add(createStyledLabel(nullToDash(r.getPrescription()), bodyFont));
        panel.add(createStyledLabel("Treatment:", bodyFont)); panel.add(createStyledLabel(nullToDash(r.getTreatment()), bodyFont));
        panel.add(createStyledLabel("Status:", bodyFont)); panel.add(createStyledLabel(nullToDash(r.getStatus()), bodyFont));
        panel.add(createStyledLabel("Notes:", bodyFont)); panel.add(createStyledLabel(nullToDash(r.getNotes()), bodyFont));
        // Symptoms and medications (aggregated strings if available)
        panel.add(createStyledLabel("Symptoms:", bodyFont)); panel.add(createStyledLabel(nullToDash(joinList(r.getSymptoms())), bodyFont));
        panel.add(createStyledLabel("Medications:", bodyFont)); panel.add(createStyledLabel(nullToDash(joinList(r.getMedications())), bodyFont));

        JOptionPane.showMessageDialog(this, panel, "Record Details", JOptionPane.PLAIN_MESSAGE);
    }

    private String nullToDash(String s) { return (s == null || s.isEmpty()) ? "-" : s; }
    private String joinList(java.util.List<String> list) {
        return (list == null || list.isEmpty()) ? "-" : String.join(", ", list);
    }
    
    private void showCreatePrescriptionDialog() {
        if (currentDoctor == null) return;
        JTextField patientIdField = createStyledTextField("");
        JTextField instructionsField = createStyledTextField("");
        JTextField validUntilField = createStyledTextField("yyyy-MM-dd (optional)");
        // Single medication entry for simplicity
        JTextField medNameField = createStyledTextField("");
        JTextField dosageField = createStyledTextField("");
        JTextField freqField = createStyledTextField("");
        JTextField durationField = createStyledTextField("");
        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        form.add(createStyledLabel("Patient ID:", bodyFont)); form.add(patientIdField);
        form.add(createStyledLabel("Instructions:", bodyFont)); form.add(instructionsField);
        form.add(createStyledLabel("Valid Until:", bodyFont)); form.add(validUntilField);
        form.add(createStyledLabel("Medication Name:", bodyFont)); form.add(medNameField);
        form.add(createStyledLabel("Dosage:", bodyFont)); form.add(dosageField);
        form.add(createStyledLabel("Frequency:", bodyFont)); form.add(freqField);
        form.add(createStyledLabel("Duration:", bodyFont)); form.add(durationField);
        int res = JOptionPane.showConfirmDialog(this, form, "Create Prescription", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            String pid = app.getDataStore().generatePrescriptionId();
            Prescription p = new Prescription(pid, patientIdField.getText().trim(), currentDoctor.getDoctorId(), null, 0);
            p.setInstructions(instructionsField.getText().trim());
            // parse date optional
            try {
                String dt = validUntilField.getText().trim();
                if (!dt.isEmpty() && dt.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    p.setValidUntil(java.time.LocalDate.parse(dt));
                }
            } catch (Exception ignore) {}
            p.addMedication(medNameField.getText().trim(), dosageField.getText().trim(), freqField.getText().trim(), durationField.getText().trim(), "");
            boolean ok = app.getDataStore().addPrescription(p);
            if (ok) {
                showSuccess("Prescription created: " + pid);
                refreshPrescriptions();
            } else {
                showError("Failed to create prescription");
            }
        }
    }
    
    private void showViewPrescriptionsDialog() {
        // Show view prescriptions dialog
        showSuccess("View prescriptions dialog would open here");
    }
}
