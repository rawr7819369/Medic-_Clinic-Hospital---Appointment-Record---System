package ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import service.AccessibilityService;

/**
 * Base frame class for all Swing UI components in MediConnect+
 * Provides common functionality and styling for all windows
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public abstract class BaseFrame extends JFrame {
    protected static Color PRIMARY_COLOR = new Color(52, 152, 219);        // Modern blue
    protected static Color SECONDARY_COLOR = new Color(155, 89, 182);      // Modern purple
    protected static Color SUCCESS_COLOR = new Color(46, 204, 113);        // Modern green
    protected static Color WARNING_COLOR = new Color(241, 196, 15);        // Modern yellow
    protected static Color ERROR_COLOR = new Color(231, 76, 60);           // Modern red
    protected static Color BACKGROUND_COLOR = new Color(236, 240, 241);    // Modern light gray
    protected static Color CARD_COLOR = new Color(255, 255, 255);          // Pure white cards
    protected static Color TEXT_COLOR = new Color(44, 62, 80);             // Modern dark gray text
    protected static Color BORDER_COLOR = new Color(189, 195, 199);        // Modern light border
    protected static Color HOVER_COLOR = new Color(52, 73, 94);            // Dark hover color
    
    protected Font titleFont;
    protected Font headerFont;
    protected Font bodyFont;
    protected Font smallFont;
    
    protected JPanel mainPanel;
    protected JPanel headerPanel;
    protected JPanel contentPanel;
    protected JPanel footerPanel;
    
    protected AccessibilityService accessibilityService;
    
    public BaseFrame(String title) {
        super(title);
        this.accessibilityService = AccessibilityService.getInstance();
        initializeFonts();
        setupFrame();
        createComponents();
        setupLayout();
        setupEventHandlers();
        applyAccessibilitySettings();
    }
    
    /**
     * Initialize fonts for different UI elements
     */
    private void initializeFonts() {
        titleFont = new Font("Segoe UI", Font.BOLD, 24);
        headerFont = new Font("Segoe UI", Font.BOLD, 18);
        bodyFont = new Font("Segoe UI", Font.PLAIN, 14);
        smallFont = new Font("Segoe UI", Font.PLAIN, 12);
    }
    
    /**
     * Setup basic frame properties
     */
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Add window listener for cleanup
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }
    
    /**
     * Create main UI components
     */
    private void createComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        headerPanel = createHeaderPanel();
        contentPanel = createContentPanel();
        footerPanel = createFooterPanel();
    }
    
    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create header panel with title and navigation
     */
    protected JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JPanel rightPanel = createHeaderRightPanel();
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create right side of header panel
     */
    protected JPanel createHeaderRightPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        
        JButton settingsButton = createStyledButton("Settings", SECONDARY_COLOR);
        settingsButton.addActionListener(e -> showSettings());
        
        JButton logoutButton = createStyledButton("Logout", ERROR_COLOR);
        logoutButton.addActionListener(e -> handleLogout());
        
        panel.add(settingsButton);
        panel.add(logoutButton);
        
        return panel;
    }
    
    /**
     * Create content panel - to be implemented by subclasses
     */
    protected abstract JPanel createContentPanel();
    
    /**
     * Create footer panel with status information
     */
    protected JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        
        JLabel statusLabel = new JLabel("MediConnect+ v1.0 - Ready");
        statusLabel.setFont(smallFont);
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(smallFont);
        timeLabel.setForeground(TEXT_COLOR);
        timeLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Update time every second
        Timer timer = new Timer(1000, e -> timeLabel.setText(java.time.LocalTime.now().toString().substring(0, 8)));
        timer.start();
        
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(timeLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create a styled button with modern appearance and rounded corners
     */
    protected JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create rounded rectangle background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Add subtle shadow effect
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(2, 2, getWidth(), getHeight(), 12, 12);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(bodyFont);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        // Enhanced hover effect with smooth transition
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Timer hoverTimer;
            private Color originalColor = backgroundColor;
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(50, null);
                hoverTimer.addActionListener(evt -> {
                    button.setBackground(blendColors(originalColor, HOVER_COLOR, 0.8f));
                    button.repaint();
                });
                hoverTimer.start();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(50, null);
                hoverTimer.addActionListener(evt -> {
                    button.setBackground(originalColor);
                    button.repaint();
                });
                hoverTimer.start();
            }
        });
        
        return button;
    }
    
    /**
     * Blend two colors smoothly
     */
    private Color blendColors(Color color1, Color color2, float ratio) {
        float inverseRatio = 1.0f - ratio;
        int red = (int) (color1.getRed() * inverseRatio + color2.getRed() * ratio);
        int green = (int) (color1.getGreen() * inverseRatio + color2.getGreen() * ratio);
        int blue = (int) (color1.getBlue() * inverseRatio + color2.getBlue() * ratio);
        return new Color(red, green, blue);
    }
    
    /**
     * Create a styled text field with modern appearance
     */
    protected JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create rounded rectangle background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        textField.setFont(bodyFont);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        textField.setBackground(CARD_COLOR);
        textField.setForeground(TEXT_COLOR);
        
        // Add focus effect
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(9, 14, 9, 14)
                ));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return textField;
    }
    
    /**
     * Create a styled password field
     */
    protected JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(bodyFont);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setBackground(CARD_COLOR);
        
        return passwordField;
    }
    
    /**
     * Create a styled combo box
     */
    protected JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(bodyFont);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setBackground(CARD_COLOR);
        
        return comboBox;
    }
    
    /**
     * Create a styled label
     */
    protected JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    /**
     * Create a card panel with modern shadow effect
     */
    protected JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create shadow effect
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 12, 12);
                
                // Create main card background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 12, 12);
                
                // Add subtle border
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 12, 12);
                
                g2.dispose();
            }
        };
        
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        return panel;
    }
    
    /**
     * Show a message dialog with custom styling
     */
    protected void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    /**
     * Show an error message
     */
    protected void showError(String message) {
        showMessage(message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show a success message
     */
    protected void showSuccess(String message) {
        showMessage(message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show a warning message
     */
    protected void showWarning(String message) {
        showMessage(message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Show a confirmation dialog
     */
    protected boolean showConfirmation(String message, String title) {
        int result = JOptionPane.showConfirmDialog(this, message, title, 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Apply accessibility settings
     */
    protected void applyAccessibilitySettings() {
        // Apply font size settings
        String fontSize = accessibilityService.getFontSize();
        applyFontSize(fontSize);
        
        // Apply theme settings
        String theme = accessibilityService.getTheme();
        applyTheme(theme);
        
        // Apply high contrast settings
        if (accessibilityService.isHighContrastEnabled()) {
            applyHighContrast();
        }
        
        // Apply large buttons settings
        if (accessibilityService.areLargeButtonsEnabled()) {
            applyLargeButtons();
        }
    }
    
    /**
     * Apply font size settings
     */
    private void applyFontSize(String fontSize) {
        Font baseFont;
        switch (fontSize.toUpperCase()) {
            case "SMALL":
                baseFont = new Font("Segoe UI", Font.PLAIN, 10);
                break;
            case "LARGE":
                baseFont = new Font("Segoe UI", Font.PLAIN, 16);
                break;
            case "EXTRA LARGE":
                baseFont = new Font("Segoe UI", Font.PLAIN, 18);
                break;
            default: // MEDIUM
                baseFont = new Font("Segoe UI", Font.PLAIN, 14);
                break;
        }
        
        // Update font sizes
        titleFont = baseFont.deriveFont(Font.BOLD, baseFont.getSize() + 10);
        headerFont = baseFont.deriveFont(Font.BOLD, baseFont.getSize() + 4);
        bodyFont = baseFont;
        smallFont = baseFont.deriveFont(baseFont.getSize() - 2);
        
        // Apply to all components
        updateComponentFonts(this, baseFont);
    }
    
    /**
     * Apply theme settings
     */
    private void applyTheme(String theme) {
        switch (theme.toUpperCase()) {
            case "DARK":
                applyDarkTheme();
                break;
            case "HIGH CONTRAST":
                applyHighContrastTheme();
                break;
            case "BLUE":
                applyBlueTheme();
                break;
            case "GREEN":
                applyGreenTheme();
                break;
            default: // LIGHT
                applyLightTheme();
                break;
        }
    }
    
    /**
     * Apply dark theme
     */
    private void applyDarkTheme() {
        BACKGROUND_COLOR = new Color(45, 45, 45);
        CARD_COLOR = new Color(60, 60, 60);
        TEXT_COLOR = new Color(220, 220, 220);
        BORDER_COLOR = new Color(100, 100, 100);
        
        updateComponentColors(this);
    }
    
    /**
     * Apply high contrast theme
     */
    private void applyHighContrastTheme() {
        BACKGROUND_COLOR = Color.WHITE;
        CARD_COLOR = Color.WHITE;
        TEXT_COLOR = Color.BLACK;
        BORDER_COLOR = Color.BLACK;
        PRIMARY_COLOR = Color.BLACK;
        SECONDARY_COLOR = Color.BLACK;
        SUCCESS_COLOR = Color.BLACK;
        ERROR_COLOR = Color.RED;
        WARNING_COLOR = Color.ORANGE;
        
        updateComponentColors(this);
    }
    
    /**
     * Apply blue theme
     */
    private void applyBlueTheme() {
        PRIMARY_COLOR = new Color(30, 144, 255);
        SECONDARY_COLOR = new Color(100, 149, 237);
        SUCCESS_COLOR = new Color(0, 191, 255);
        WARNING_COLOR = new Color(255, 165, 0);
        ERROR_COLOR = new Color(220, 20, 60);
        
        updateComponentColors(this);
    }
    
    /**
     * Apply green theme
     */
    private void applyGreenTheme() {
        PRIMARY_COLOR = new Color(34, 139, 34);
        SECONDARY_COLOR = new Color(50, 205, 50);
        SUCCESS_COLOR = new Color(0, 255, 0);
        WARNING_COLOR = new Color(255, 215, 0);
        ERROR_COLOR = new Color(255, 69, 0);
        
        updateComponentColors(this);
    }
    
    /**
     * Apply light theme (default)
     */
    private void applyLightTheme() {
        // Reset to modern colors with better contrast
        PRIMARY_COLOR = new Color(52, 152, 219);        // Modern blue
        SECONDARY_COLOR = new Color(155, 89, 182);      // Modern purple
        SUCCESS_COLOR = new Color(46, 204, 113);        // Modern green
        WARNING_COLOR = new Color(241, 196, 15);        // Modern yellow
        ERROR_COLOR = new Color(231, 76, 60);           // Modern red
        BACKGROUND_COLOR = new Color(236, 240, 241);    // Modern light gray
        CARD_COLOR = new Color(255, 255, 255);          // Pure white cards
        TEXT_COLOR = new Color(44, 62, 80);             // Modern dark gray text
        BORDER_COLOR = new Color(189, 195, 199);        // Modern light border
        HOVER_COLOR = new Color(52, 73, 94);            // Dark hover color
        
        updateComponentColors(this);
    }
    
    /**
     * Apply high contrast mode
     */
    private void applyHighContrast() {
        // Increase contrast for better visibility
        BORDER_COLOR = Color.BLACK;
        TEXT_COLOR = Color.BLACK;
        
        // Make borders thicker
        updateComponentBorders(this, 2);
    }
    
    /**
     * Apply large buttons
     */
    private void applyLargeButtons() {
        // Increase button sizes
        updateButtonSizes(this, 1.5f);
    }
    
    /**
     * Update component fonts recursively
     */
    private void updateComponentFonts(Container container, Font font) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel) {
                ((JLabel) component).setFont(font);
            } else if (component instanceof JButton) {
                ((JButton) component).setFont(font);
            } else if (component instanceof JTextField) {
                ((JTextField) component).setFont(font);
            } else if (component instanceof JPasswordField) {
                ((JPasswordField) component).setFont(font);
            } else if (component instanceof JTextArea) {
                ((JTextArea) component).setFont(font);
            } else if (component instanceof JComboBox) {
                ((JComboBox<?>) component).setFont(font);
            } else if (component instanceof JTable) {
                ((JTable) component).setFont(font);
            } else if (component instanceof Container) {
                updateComponentFonts((Container) component, font);
            }
        }
    }
    
    /**
     * Update component colors recursively
     */
    private void updateComponentColors(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel) {
                ((JLabel) component).setForeground(TEXT_COLOR);
            } else if (component instanceof JTextField) {
                ((JTextField) component).setBackground(CARD_COLOR);
                ((JTextField) component).setForeground(TEXT_COLOR);
            } else if (component instanceof JPasswordField) {
                ((JPasswordField) component).setBackground(CARD_COLOR);
                ((JPasswordField) component).setForeground(TEXT_COLOR);
            } else if (component instanceof JTextArea) {
                ((JTextArea) component).setBackground(CARD_COLOR);
                ((JTextArea) component).setForeground(TEXT_COLOR);
            } else if (component instanceof JComboBox) {
                ((JComboBox<?>) component).setBackground(CARD_COLOR);
                ((JComboBox<?>) component).setForeground(TEXT_COLOR);
            } else if (component instanceof JTable) {
                ((JTable) component).setBackground(CARD_COLOR);
                ((JTable) component).setForeground(TEXT_COLOR);
            } else if (component instanceof Container) {
                updateComponentColors((Container) component);
            }
        }
    }
    
    /**
     * Update component borders recursively
     */
    private void updateComponentBorders(Container container, int thickness) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField) {
                ((JTextField) component).setBorder(BorderFactory.createLineBorder(BORDER_COLOR, thickness));
            } else if (component instanceof JPasswordField) {
                ((JPasswordField) component).setBorder(BorderFactory.createLineBorder(BORDER_COLOR, thickness));
            } else if (component instanceof JTextArea) {
                ((JTextArea) component).setBorder(BorderFactory.createLineBorder(BORDER_COLOR, thickness));
            } else if (component instanceof JComboBox) {
                ((JComboBox<?>) component).setBorder(BorderFactory.createLineBorder(BORDER_COLOR, thickness));
            } else if (component instanceof Container) {
                updateComponentBorders((Container) component, thickness);
            }
        }
    }
    
    /**
     * Update button sizes recursively
     */
    private void updateButtonSizes(Container container, float scale) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                Dimension currentSize = button.getPreferredSize();
                button.setPreferredSize(new Dimension(
                    (int) (currentSize.width * scale),
                    (int) (currentSize.height * scale)
                ));
            } else if (component instanceof Container) {
                updateButtonSizes((Container) component, scale);
            }
        }
    }
    
    /**
     * Handle window closing event
     */
    protected void handleWindowClosing() {
        if (showConfirmation("Are you sure you want to exit?", "Exit Application")) {
            System.exit(0);
        }
    }
    
    /**
     * Handle logout action
     */
    protected void handleLogout() {
        if (showConfirmation("Are you sure you want to logout?", "Logout")) {
            dispose();
            // Return to login screen instead of closing the entire program
            SwingUtilities.invokeLater(() -> {
                try {
                    // Get the main app instance and show login screen
                    if (this instanceof LoginFrame) {
                        // If we're already on login screen, just dispose
                        System.exit(0);
                    } else {
                        // For other frames, show login screen
                        MediConnectSwingApp app = MediConnectSwingApp.getInstance();
                        app.showLoginScreen();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Fallback: just exit if there's an error
                    System.exit(0);
                }
            });
        }
    }
    
    /**
     * Show settings dialog
     */
    protected void showSettings() {
        // This will be implemented to show accessibility settings
        showMessage("Settings functionality will be implemented", "Settings", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Setup event handlers - to be implemented by subclasses
     */
    protected abstract void setupEventHandlers();
    
    /**
     * Get the title for this frame
     */
    public abstract String getTitle();
}
