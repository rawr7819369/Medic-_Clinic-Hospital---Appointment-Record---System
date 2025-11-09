package ui.swing;

import model.*;
import util.InputValidator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Book appointment dialog for patients to book new appointments
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class BookAppointmentDialog extends JDialog {
    private MediConnectSwingApp app;
    private Patient currentPatient;
    
    private JComboBox<String> doctorComboBox;
    private JTextField dateField;
    private JComboBox<String> timeSlotComboBox;
    private JTextArea reasonArea;
    
    private JButton bookButton;
    private JButton cancelButton;
    private JButton checkAvailabilityButton;
    
    public BookAppointmentDialog(JFrame parent, MediConnectSwingApp app, Patient patient) {
        super(parent, "Create Appointment", true);
        this.app = app;
        this.currentPatient = patient;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDoctors();
        
        pack();
        setMinimumSize(new Dimension(700, 580));
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialize all components
     */
    private void initializeComponents() {
        // Doctor selection
        doctorComboBox = new JComboBox<>();
        doctorComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Date field
        dateField = new JTextField(20);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Time slot selection
        timeSlotComboBox = new JComboBox<>();
        timeSlotComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Reason text area
        reasonArea = new JTextArea(4, 20);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        
        // Buttons
        bookButton = new JButton("Book Appointment");
        cancelButton = new JButton("Cancel");
        checkAvailabilityButton = new JButton("Check Availability");
        
        styleComponents();
    }
    
    /**
     * Style all components
     */
    private void styleComponents() {
        Font bodyFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        // Style combo boxes
        doctorComboBox.setFont(bodyFont);
        timeSlotComboBox.setFont(bodyFont);
        
        // Style buttons with solid backgrounds to ensure text visibility
        bookButton.setBackground(new Color(46, 204, 113));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(bodyFont);
        bookButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.setFocusPainted(false);
        bookButton.setContentAreaFilled(true);
        bookButton.setOpaque(true);
        bookButton.setBorderPainted(false);
        bookButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        bookButton.setMargin(new Insets(8,16,8,16));
        bookButton.setPreferredSize(new Dimension(150, 35));
        
        // Remove custom hover that relied on background color (to keep LAF consistent)
        
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(bodyFont);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setFocusPainted(false);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        cancelButton.setMargin(new Insets(8,16,8,16));
        cancelButton.setPreferredSize(new Dimension(110, 35));
        
        // Remove custom hover coloring
        
        checkAvailabilityButton.setBackground(new Color(52, 152, 219));
        checkAvailabilityButton.setForeground(Color.WHITE);
        checkAvailabilityButton.setFont(bodyFont);
        checkAvailabilityButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        checkAvailabilityButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkAvailabilityButton.setFocusPainted(false);
        checkAvailabilityButton.setContentAreaFilled(true);
        checkAvailabilityButton.setOpaque(true);
        checkAvailabilityButton.setBorderPainted(false);
        checkAvailabilityButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        checkAvailabilityButton.setMargin(new Insets(6,14,6,14));
        checkAvailabilityButton.setPreferredSize(new Dimension(170, 36));
        // Ensure buttons are enabled
        bookButton.setEnabled(true);
        cancelButton.setEnabled(true);
        checkAvailabilityButton.setEnabled(true);
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
        JLabel titleLabel = new JLabel("Create Appointment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        buttonPanel.add(bookButton);
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
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[]{160, 380, 200};
        gbl.columnWeights = new double[]{0.0, 1.0, 0.0};
        JPanel panel = new JPanel(gbl);
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        
        int row = 0;
        
        // Doctor selection
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel doctorLabel = new JLabel("Doctor:");
        doctorLabel.setFont(labelFont);
        doctorLabel.setForeground(new Color(52, 73, 94));
        doctorLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(doctorLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        doctorComboBox.setFont(fieldFont);
        doctorComboBox.setPreferredSize(new Dimension(360, 34));
        doctorComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        panel.add(doctorComboBox, gbc);
        
        row++;
        
        // Date
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(labelFont);
        dateLabel.setForeground(new Color(52, 73, 94));
        dateLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(dateLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        dateField.setFont(fieldFont);
        dateField.setPreferredSize(new Dimension(360, 34));
        panel.add(dateField, gbc);
        
        // Place 'Check Availability' in the same row as Date, right side (third column)
        gbc.gridx = 2; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE; gbc.insets = new Insets(0, 8, 0, 0);
        panel.add(checkAvailabilityButton, gbc);
        
        row++;
        
        // Add an empty placeholder to occupy the third column for alignment in subsequent rows
        gbc.gridx = 2; gbc.gridy = row-1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        panel.add(Box.createHorizontalStrut(1), gbc);

        // Time slot
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8);
        JLabel timeLabel = new JLabel("Time Slot:");
        timeLabel.setFont(labelFont);
        timeLabel.setForeground(new Color(52, 73, 94));
        timeLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(timeLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        timeSlotComboBox.setFont(fieldFont);
        timeSlotComboBox.setPreferredSize(new Dimension(360, 34));
        timeSlotComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        panel.add(timeSlotComboBox, gbc);
        
        row++;
        
        // Reason
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel reasonLabel = new JLabel("Reason:");
        reasonLabel.setFont(labelFont);
        reasonLabel.setForeground(new Color(52, 73, 94));
        reasonLabel.setPreferredSize(new Dimension(140, 20));
        panel.add(reasonLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        reasonArea.setFont(fieldFont);
        reasonArea.setPreferredSize(new Dimension(360, 120));
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setPreferredSize(new Dimension(360, 120));
        panel.add(scrollPane, gbc);
        
        return panel;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBookAppointment();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        checkAvailabilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAvailability();
            }
        });
        
        doctorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear time slots when doctor changes
                timeSlotComboBox.removeAllItems();
            }
        });
    }
    
    /**
     * Load available doctors
     */
    private void loadDoctors() {
        List<Doctor> doctors = new java.util.ArrayList<>(app.getDataStore().getAllDoctors());
        for (Doctor doctor : doctors) {
            if (doctor.isActive()) {
                doctorComboBox.addItem(doctor.getDoctorId() + " - " + doctor.getFullName() + " (" + doctor.getSpecialization() + ")");
            }
        }
    }
    
    /**
     * Check availability for selected doctor and date
     */
    private void checkAvailability() {
        String selectedDoctor = (String) doctorComboBox.getSelectedItem();
        String dateStr = dateField.getText().trim();
        
        if (selectedDoctor == null || selectedDoctor.isEmpty()) {
            showError("Please select a doctor.");
            return;
        }
        
        if (dateStr.isEmpty()) {
            showError("Please enter a date.");
            dateField.requestFocus();
            return;
        }
        
        if (!InputValidator.isValidDate(dateStr)) {
            showError("Please enter a valid date in yyyy-MM-dd format.");
            dateField.requestFocus();
            return;
        }
        
        if (!InputValidator.isFutureDate(dateStr)) {
            showError("Please enter a future date.");
            dateField.requestFocus();
            return;
        }
        
        // Extract doctor ID
        String doctorId = selectedDoctor.split(" - ")[0];
        
        // Get available time slots
        LocalDate appointmentDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<String> availableSlots = app.getAppointmentService().getAvailableTimeSlots(doctorId, appointmentDate);
        
        // Update time slot combo box
        timeSlotComboBox.removeAllItems();
        if (availableSlots.isEmpty()) {
            showWarning("No available time slots for the selected date.");
        } else {
            for (String slot : availableSlots) {
                timeSlotComboBox.addItem(slot);
            }
            showSuccess("Found " + availableSlots.size() + " available time slots.");
        }
    }
    
    /**
     * Handle book appointment process
     */
    private void handleBookAppointment() {
        String selectedDoctor = (String) doctorComboBox.getSelectedItem();
        String dateStr = dateField.getText().trim();
        String timeSlot = (String) timeSlotComboBox.getSelectedItem();
        String reason = reasonArea.getText().trim();
        
        // Validate input
        if (!validateInput(selectedDoctor, dateStr, timeSlot, reason)) {
            return;
        }
        
        // Extract doctor ID
        String doctorId = selectedDoctor.split(" - ")[0];
        
        // Book appointment
        Appointment appointment = app.getAppointmentService().bookAppointment(
            doctorId, currentPatient.getPatientId(), dateStr, timeSlot, reason
        );
        
        if (appointment != null) {
            JOptionPane.showMessageDialog(this, 
                "Appointment booked successfully!\nAppointment ID: " + appointment.getAppointmentId(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to book appointment. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate form input
     */
    private boolean validateInput(String selectedDoctor, String dateStr, String timeSlot, String reason) {
        if (selectedDoctor == null || selectedDoctor.isEmpty()) {
            showError("Please select a doctor.");
            doctorComboBox.requestFocus();
            return false;
        }
        
        if (dateStr.isEmpty()) {
            showError("Please enter a date.");
            dateField.requestFocus();
            return false;
        }
        
        if (!InputValidator.isValidDate(dateStr)) {
            showError("Please enter a valid date in yyyy-MM-dd format.");
            dateField.requestFocus();
            return false;
        }
        
        if (!InputValidator.isFutureDate(dateStr)) {
            showError("Please enter a future date.");
            dateField.requestFocus();
            return false;
        }
        
        if (timeSlot == null || timeSlot.isEmpty()) {
            showError("Please select a time slot.");
            timeSlotComboBox.requestFocus();
            return false;
        }
        
        if (reason.isEmpty()) {
            showError("Please enter a reason for the appointment.");
            reasonArea.requestFocus();
            return false;
        }
        
        if (!InputValidator.isValidAppointmentReason(reason)) {
            showError("Please enter a valid reason for the appointment.");
            reasonArea.requestFocus();
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
    
    /**
     * Show success message
     */
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show warning message
     */
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
