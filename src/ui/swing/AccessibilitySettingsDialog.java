package ui.swing;

import service.AccessibilityService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Accessibility settings dialog for customizing UI appearance and behavior
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class AccessibilitySettingsDialog extends JDialog {
    private AccessibilityService accessibilityService;
    private JFrame parentFrame;
    
    private JComboBox<String> fontSizeComboBox;
    private JComboBox<String> themeComboBox;
    
    private JButton applyButton;
    private JButton cancelButton;
    private JButton resetButton;
    
    public AccessibilitySettingsDialog(JFrame parent, MediConnectSwingApp app) {
        super(parent, "Accessibility Settings", true);
        this.parentFrame = parent;
        this.accessibilityService = app.getAccessibilityService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCurrentSettings();
        
        setSize(500, 600);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialize all components
     */
    private void initializeComponents() {
        // Font size options
        String[] fontSizes = {"Small", "Medium", "Large", "Extra Large"};
        fontSizeComboBox = new JComboBox<>(fontSizes);
        
        // Theme options
        String[] themes = {"Light", "Dark", "High Contrast", "Blue", "Green"};
        themeComboBox = new JComboBox<>(themes);
        
        // Buttons
        applyButton = new JButton("Apply Settings");
        cancelButton = new JButton("Cancel");
        resetButton = new JButton("Reset to Default");
        
        // Style components
        styleComponents();
    }
    
    /**
     * Style all components
     */
    private void styleComponents() {
        Font bodyFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        // Style combo boxes
        fontSizeComboBox.setFont(bodyFont);
        themeComboBox.setFont(bodyFont);
        
        // Style buttons
        applyButton.setBackground(new Color(46, 204, 113)); // SUCCESS_COLOR - Green for positive action
        applyButton.setForeground(Color.WHITE);
        applyButton.setFont(bodyFont);
        applyButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        applyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyButton.setFocusPainted(false);
        applyButton.setContentAreaFilled(true);
        applyButton.setOpaque(true);
        
        cancelButton.setBackground(new Color(231, 76, 60)); // ERROR_COLOR - Red for negative action
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(bodyFont);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setFocusPainted(false);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setOpaque(true);
        
        resetButton.setBackground(new Color(241, 196, 15)); // WARNING_COLOR - Yellow for warning action
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(bodyFont);
        resetButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.setFocusPainted(false);
        resetButton.setContentAreaFilled(true);
        resetButton.setOpaque(true);
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
        JLabel titleLabel = new JLabel("Accessibility Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Settings panel
        JPanel settingsPanel = createSettingsPanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(settingsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create the settings panel
     */
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        
        // Font Size
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Font Size:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(fontSizeComboBox, gbc);
        
        // Theme
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Theme:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(themeComboBox, gbc);
        
        // Optional: keep spacing consistent below theme
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(Box.createVerticalStrut(10), gbc);
        
        return panel;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySettings();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetToDefault();
            }
        });
    }
    
    /**
     * Load current accessibility settings
     */
    private void loadCurrentSettings() {
        // Set font size
        String currentFontSize = accessibilityService.getFontSize();
        fontSizeComboBox.setSelectedItem(currentFontSize);
        
        // Set theme
        String currentTheme = accessibilityService.getTheme();
        themeComboBox.setSelectedItem(currentTheme);
        
        // No checkbox features to load anymore
    }
    
    /**
     * Apply accessibility settings
     */
    private void applySettings() {
        // Apply font size
        String selectedFontSize = (String) fontSizeComboBox.getSelectedItem();
        accessibilityService.setFontSize(selectedFontSize);
        
        // Apply theme
        String selectedTheme = (String) themeComboBox.getSelectedItem();
        accessibilityService.setTheme(selectedTheme);
        
        // Immediately apply settings to all open BaseFrame windows
        for (java.awt.Window w : java.awt.Window.getWindows()) {
            if (w instanceof BaseFrame) {
                ((BaseFrame) w).applyAccessibilitySettings();
                w.repaint();
            }
        }
        
        JOptionPane.showMessageDialog(this, 
            "Accessibility settings applied successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }
    
    /**
     * Reset to default settings
     */
    private void resetToDefault() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to reset all settings to default?",
            "Reset Settings", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Reset to default values
            fontSizeComboBox.setSelectedItem("Medium");
            themeComboBox.setSelectedItem("Light");
            // Persist defaults
            accessibilityService.setFontSize("Medium");
            accessibilityService.setTheme("Light");
            // Apply to all open frames immediately
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof BaseFrame) {
                    ((BaseFrame) w).applyAccessibilitySettings();
                    w.repaint();
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "Settings reset to default values.",
                "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
