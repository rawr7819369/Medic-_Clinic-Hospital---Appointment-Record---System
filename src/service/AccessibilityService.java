package service;

import util.ConsolePrinter;
import java.util.HashMap;
import java.util.Map;

/**
 * AccessibilityService class for managing accessibility features and user preferences.
 * Implements accessibility options like font size, theme, and text-to-speech simulation.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class AccessibilityService {
    private static AccessibilityService instance;
    private final Map<String, Object> accessibilitySettings;
    
    // Default accessibility settings
    private static final String FONT_SIZE_KEY = "fontSize";
    private static final String THEME_KEY = "theme";
    private static final String TEXT_TO_SPEECH_KEY = "textToSpeech";
    private static final String HIGH_CONTRAST_KEY = "highContrast";
    private static final String VOICE_ASSISTANCE_KEY = "voiceAssistance";
    private static final String LARGE_BUTTONS_KEY = "largeButtons";
    private static final String SCREEN_READER_KEY = "screenReader";
    
    // Available options
    private static final String[] FONT_SIZES = {"Small", "Medium", "Large", "Extra Large"};
    private static final String[] THEMES = {"Light", "Dark", "High Contrast", "Blue", "Green"};
    private static final String[] YES_NO_OPTIONS = {"Yes", "No"};
    
    /**
     * Private constructor for singleton pattern
     */
    private AccessibilityService() {
        this.accessibilitySettings = new HashMap<>();
        initializeDefaultSettings();
    }
    
    /**
     * Get singleton instance of AccessibilityService
     * @return AccessibilityService instance
     */
    public static synchronized AccessibilityService getInstance() {
        if (instance == null) {
            instance = new AccessibilityService();
        }
        return instance;
    }
    
    /**
     * Initialize default accessibility settings
     */
    private void initializeDefaultSettings() {
        accessibilitySettings.put(FONT_SIZE_KEY, "Medium");
        accessibilitySettings.put(THEME_KEY, "Light");
        accessibilitySettings.put(TEXT_TO_SPEECH_KEY, false);
        accessibilitySettings.put(HIGH_CONTRAST_KEY, false);
        accessibilitySettings.put(VOICE_ASSISTANCE_KEY, false);
        accessibilitySettings.put(LARGE_BUTTONS_KEY, false);
        accessibilitySettings.put(SCREEN_READER_KEY, false);
    }
    
    /**
     * Display accessibility settings menu
     */
    public void displayAccessibilityMenu() {
        ConsolePrinter.printHeader("Accessibility Settings");
        
        String[] menuOptions = {
            "Font Size",
            "Theme",
            "Text-to-Speech",
            "High Contrast Mode",
            "Voice Assistance",
            "Large Buttons",
            "Screen Reader",
            "Reset to Defaults",
            "View Current Settings",
            "Back to Main Menu"
        };
        
        ConsolePrinter.printMenu("Accessibility Options", menuOptions);
    }
    
    /**
     * Handle accessibility settings based on user choice
     * @param choice user's menu choice
     * @return true if settings were updated, false otherwise
     */
    public boolean handleAccessibilityChoice(int choice) {
        switch (choice) {
            case 1:
                return setFontSize();
            case 2:
                return setTheme();
            case 3:
                return setTextToSpeech();
            case 4:
                return setHighContrast();
            case 5:
                return setVoiceAssistance();
            case 6:
                return setLargeButtons();
            case 7:
                return setScreenReader();
            case 8:
                return resetToDefaults();
            case 9:
                displayCurrentSettings();
                return true;
            case 10:
                return false; // Back to main menu
            default:
                ConsolePrinter.printError("Invalid choice. Please try again.");
                return false;
        }
    }
    
    /**
     * Set font size preference
     * @return true if setting was updated, false otherwise
     */
    public boolean setFontSize() {
        ConsolePrinter.printSubHeader("Font Size Settings");
        ConsolePrinter.printList("Available Font Sizes", java.util.Arrays.asList(FONT_SIZES));
        
        int choice = ConsolePrinter.getIntInput("Select font size (1-4)", 1, 4);
        String selectedSize = FONT_SIZES[choice - 1];
        
        accessibilitySettings.put(FONT_SIZE_KEY, selectedSize);
        ConsolePrinter.printSuccess("Font size set to: " + selectedSize);
        
        // Simulate font size change
        simulateFontSizeChange(selectedSize);
        return true;
    }
    
    /**
     * Set theme preference
     * @return true if setting was updated, false otherwise
     */
    public boolean setTheme() {
        ConsolePrinter.printSubHeader("Theme Settings");
        ConsolePrinter.printList("Available Themes", java.util.Arrays.asList(THEMES));
        
        int choice = ConsolePrinter.getIntInput("Select theme (1-5)", 1, 5);
        String selectedTheme = THEMES[choice - 1];
        
        accessibilitySettings.put(THEME_KEY, selectedTheme);
        ConsolePrinter.printSuccess("Theme set to: " + selectedTheme);
        
        // Simulate theme change
        simulateThemeChange(selectedTheme);
        return true;
    }
    
    /**
     * Set text-to-speech preference
     * @return true if setting was updated, false otherwise
     */
    public boolean setTextToSpeech() {
        ConsolePrinter.printSubHeader("Text-to-Speech Settings");
        ConsolePrinter.printList("Text-to-Speech Options", java.util.Arrays.asList(YES_NO_OPTIONS));
        
        int choice = ConsolePrinter.getIntInput("Enable text-to-speech? (1-2)", 1, 2);
        boolean enabled = choice == 1;
        
        accessibilitySettings.put(TEXT_TO_SPEECH_KEY, enabled);
        ConsolePrinter.printSuccess("Text-to-speech " + (enabled ? "enabled" : "disabled"));
        
        if (enabled) {
            simulateTextToSpeech("Text-to-speech has been enabled. This feature will read aloud important information.");
        }
        return true;
    }
    
    /**
     * Set high contrast mode preference
     * @return true if setting was updated, false otherwise
     */
    public boolean setHighContrast() {
        ConsolePrinter.printSubHeader("High Contrast Mode Settings");
        ConsolePrinter.printList("High Contrast Options", java.util.Arrays.asList(YES_NO_OPTIONS));
        
        int choice = ConsolePrinter.getIntInput("Enable high contrast mode? (1-2)", 1, 2);
        boolean enabled = choice == 1;
        
        accessibilitySettings.put(HIGH_CONTRAST_KEY, enabled);
        ConsolePrinter.printSuccess("High contrast mode " + (enabled ? "enabled" : "disabled"));
        
        if (enabled) {
            simulateHighContrastMode();
        }
        return true;
    }
    
    /**
     * Set voice assistance preference
     * @return true if setting was updated, false otherwise
     */
    public boolean setVoiceAssistance() {
        ConsolePrinter.printSubHeader("Voice Assistance Settings");
        ConsolePrinter.printList("Voice Assistance Options", java.util.Arrays.asList(YES_NO_OPTIONS));
        
        int choice = ConsolePrinter.getIntInput("Enable voice assistance? (1-2)", 1, 2);
        boolean enabled = choice == 1;
        
        accessibilitySettings.put(VOICE_ASSISTANCE_KEY, enabled);
        ConsolePrinter.printSuccess("Voice assistance " + (enabled ? "enabled" : "disabled"));
        
        if (enabled) {
            simulateVoiceAssistance("Voice assistance has been enabled. You can now use voice commands to navigate the system.");
        }
        return true;
    }
    
    /**
     * Set large buttons preference
     * @return true if setting was updated, false otherwise
     */
    public boolean setLargeButtons() {
        ConsolePrinter.printSubHeader("Large Buttons Settings");
        ConsolePrinter.printList("Large Buttons Options", java.util.Arrays.asList(YES_NO_OPTIONS));
        
        int choice = ConsolePrinter.getIntInput("Enable large buttons? (1-2)", 1, 2);
        boolean enabled = choice == 1;
        
        accessibilitySettings.put(LARGE_BUTTONS_KEY, enabled);
        ConsolePrinter.printSuccess("Large buttons " + (enabled ? "enabled" : "disabled"));
        
        if (enabled) {
            simulateLargeButtons();
        }
        return true;
    }
    
    /**
     * Set screen reader preference
     * @return true if setting was updated, false otherwise
     */
    public boolean setScreenReader() {
        ConsolePrinter.printSubHeader("Screen Reader Settings");
        ConsolePrinter.printList("Screen Reader Options", java.util.Arrays.asList(YES_NO_OPTIONS));
        
        int choice = ConsolePrinter.getIntInput("Enable screen reader? (1-2)", 1, 2);
        boolean enabled = choice == 1;
        
        accessibilitySettings.put(SCREEN_READER_KEY, enabled);
        ConsolePrinter.printSuccess("Screen reader " + (enabled ? "enabled" : "disabled"));
        
        if (enabled) {
            simulateScreenReader("Screen reader has been enabled. The system will provide audio descriptions of interface elements.");
        }
        return true;
    }
    
    /**
     * Reset all settings to defaults
     * @return true if settings were reset, false otherwise
     */
    public boolean resetToDefaults() {
        if (ConsolePrinter.printConfirmation("Are you sure you want to reset all accessibility settings to defaults?")) {
            initializeDefaultSettings();
            ConsolePrinter.printSuccess("All accessibility settings have been reset to defaults");
            return true;
        } else {
            ConsolePrinter.printInfo("Settings reset cancelled");
            return false;
        }
    }
    
    /**
     * Display current accessibility settings
     */
    public void displayCurrentSettings() {
        ConsolePrinter.printSubHeader("Current Accessibility Settings");
        
        ConsolePrinter.printInfo("Font Size: " + accessibilitySettings.get(FONT_SIZE_KEY));
        ConsolePrinter.printInfo("Theme: " + accessibilitySettings.get(THEME_KEY));
        ConsolePrinter.printInfo("Text-to-Speech: " + (Boolean) accessibilitySettings.get(TEXT_TO_SPEECH_KEY));
        ConsolePrinter.printInfo("High Contrast Mode: " + (Boolean) accessibilitySettings.get(HIGH_CONTRAST_KEY));
        ConsolePrinter.printInfo("Voice Assistance: " + (Boolean) accessibilitySettings.get(VOICE_ASSISTANCE_KEY));
        ConsolePrinter.printInfo("Large Buttons: " + (Boolean) accessibilitySettings.get(LARGE_BUTTONS_KEY));
        ConsolePrinter.printInfo("Screen Reader: " + (Boolean) accessibilitySettings.get(SCREEN_READER_KEY));
    }
    
    /**
     * Get current font size setting
     * @return current font size
     */
    public String getFontSize() {
        return (String) accessibilitySettings.get(FONT_SIZE_KEY);
    }
    
    /**
     * Get current theme setting
     * @return current theme
     */
    public String getTheme() {
        return (String) accessibilitySettings.get(THEME_KEY);
    }
    
    /**
     * Check if text-to-speech is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isTextToSpeechEnabled() {
        return (Boolean) accessibilitySettings.get(TEXT_TO_SPEECH_KEY);
    }
    
    /**
     * Check if high contrast mode is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isHighContrastEnabled() {
        return (Boolean) accessibilitySettings.get(HIGH_CONTRAST_KEY);
    }
    
    /**
     * Check if voice assistance is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isVoiceAssistanceEnabled() {
        return (Boolean) accessibilitySettings.get(VOICE_ASSISTANCE_KEY);
    }
    
    /**
     * Check if large buttons are enabled
     * @return true if enabled, false otherwise
     */
    public boolean areLargeButtonsEnabled() {
        return (Boolean) accessibilitySettings.get(LARGE_BUTTONS_KEY);
    }
    
    /**
     * Check if screen reader is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isScreenReaderEnabled() {
        return (Boolean) accessibilitySettings.get(SCREEN_READER_KEY);
    }
    
    /**
     * Simulate font size change
     * @param fontSize selected font size
     */
    private void simulateFontSizeChange(String fontSize) {
        ConsolePrinter.printInfo("Simulating font size change to " + fontSize + "...");
        ConsolePrinter.printLoading("Applying font size");
        ConsolePrinter.printSuccess("Font size applied successfully");
    }
    
    /**
     * Simulate theme change
     * @param theme selected theme
     */
    private void simulateThemeChange(String theme) {
        ConsolePrinter.printInfo("Simulating theme change to " + theme + "...");
        ConsolePrinter.printLoading("Applying theme");
        ConsolePrinter.printSuccess("Theme applied successfully");
    }
    
    /**
     * Simulate text-to-speech
     * @param text text to be spoken
     */
    private void simulateTextToSpeech(String text) {
        ConsolePrinter.printInfo("🔊 Text-to-Speech: " + text);
        ConsolePrinter.printLoading("Speaking text");
    }
    
    /**
     * Simulate high contrast mode
     */
    private void simulateHighContrastMode() {
        ConsolePrinter.printInfo("🎨 High contrast mode activated");
        ConsolePrinter.printInfo("Interface colors have been adjusted for better visibility");
    }
    
    /**
     * Simulate voice assistance
     * @param message voice assistance message
     */
    private void simulateVoiceAssistance(String message) {
        ConsolePrinter.printInfo("🎤 Voice Assistance: " + message);
        ConsolePrinter.printInfo("You can now say commands like 'Navigate to appointments' or 'Book new appointment'");
    }
    
    /**
     * Simulate large buttons
     */
    private void simulateLargeButtons() {
        ConsolePrinter.printInfo("🔘 Large buttons activated");
        ConsolePrinter.printInfo("All interface buttons have been enlarged for easier interaction");
    }
    
    /**
     * Simulate screen reader
     * @param message screen reader message
     */
    private void simulateScreenReader(String message) {
        ConsolePrinter.printInfo("📖 Screen Reader: " + message);
        ConsolePrinter.printInfo("Interface elements will be described audibly");
    }
    
    /**
     * Apply accessibility settings to console output
     * @param text text to format
     * @return formatted text based on accessibility settings
     */
    public String applyAccessibilityFormatting(String text) {
        String formattedText = text;
        
        // Apply font size simulation
        String fontSize = getFontSize();
        if (fontSize.equals("Large") || fontSize.equals("Extra Large")) {
            formattedText = "[LARGE] " + formattedText;
        }
        
        // Apply high contrast simulation
        if (isHighContrastEnabled()) {
            formattedText = "[HIGH_CONTRAST] " + formattedText + " [END_CONTRAST]";
        }
        
        // Apply text-to-speech simulation
        if (isTextToSpeechEnabled()) {
            formattedText = "[TTS] " + formattedText;
        }
        
        return formattedText;
    }
    
    /**
     * Set font size directly (for Swing UI)
     * @param fontSize font size to set
     */
    public void setFontSize(String fontSize) {
        accessibilitySettings.put(FONT_SIZE_KEY, fontSize);
    }
    
    /**
     * Set theme directly (for Swing UI)
     * @param theme theme to set
     */
    public void setTheme(String theme) {
        accessibilitySettings.put(THEME_KEY, theme);
    }
    
    /**
     * Set text-to-speech enabled directly (for Swing UI)
     * @param enabled whether to enable text-to-speech
     */
    public void setTextToSpeechEnabled(boolean enabled) {
        accessibilitySettings.put(TEXT_TO_SPEECH_KEY, enabled);
    }
    
    /**
     * Set high contrast enabled directly (for Swing UI)
     * @param enabled whether to enable high contrast
     */
    public void setHighContrastEnabled(boolean enabled) {
        accessibilitySettings.put(HIGH_CONTRAST_KEY, enabled);
    }
    
    /**
     * Set voice assistance enabled directly (for Swing UI)
     * @param enabled whether to enable voice assistance
     */
    public void setVoiceAssistanceEnabled(boolean enabled) {
        accessibilitySettings.put(VOICE_ASSISTANCE_KEY, enabled);
    }
    
    /**
     * Set large buttons enabled directly (for Swing UI)
     * @param enabled whether to enable large buttons
     */
    public void setLargeButtonsEnabled(boolean enabled) {
        accessibilitySettings.put(LARGE_BUTTONS_KEY, enabled);
    }
    
    /**
     * Set screen reader enabled directly (for Swing UI)
     * @param enabled whether to enable screen reader
     */
    public void setScreenReaderEnabled(boolean enabled) {
        accessibilitySettings.put(SCREEN_READER_KEY, enabled);
    }
    
    /**
     * Get accessibility settings summary
     * @return formatted accessibility settings summary
     */
    public String getAccessibilitySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== ACCESSIBILITY SETTINGS SUMMARY ===\n");
        summary.append("Font Size: ").append(getFontSize()).append("\n");
        summary.append("Theme: ").append(getTheme()).append("\n");
        summary.append("Text-to-Speech: ").append(isTextToSpeechEnabled() ? "Enabled" : "Disabled").append("\n");
        summary.append("High Contrast: ").append(isHighContrastEnabled() ? "Enabled" : "Disabled").append("\n");
        summary.append("Voice Assistance: ").append(isVoiceAssistanceEnabled() ? "Enabled" : "Disabled").append("\n");
        summary.append("Large Buttons: ").append(areLargeButtonsEnabled() ? "Enabled" : "Disabled").append("\n");
        summary.append("Screen Reader: ").append(isScreenReaderEnabled() ? "Enabled" : "Disabled").append("\n");
        return summary.toString();
    }
}
