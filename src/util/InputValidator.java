package util;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * InputValidator utility class for validating user inputs.
 * Provides static methods for various validation operations.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class InputValidator {
    
    // Regular expression patterns for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$"
    );
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Validate email address format
     * @param email email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number format
     * @param phone phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Remove spaces, dashes, and parentheses for validation
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Validate username format
     * @param username username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }
    
    /**
     * Validate password strength
     * @param password password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Validate that two passwords match
     * @param password original password
     * @param confirmPassword confirmation password
     * @return true if passwords match, false otherwise
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
    
    /**
     * Validate name format (letters, spaces, hyphens, apostrophes)
     * @param name name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.trim().matches("^[a-zA-Z\\s\\-']{2,50}$");
    }
    
    /**
     * Validate age range
     * @param age age to validate
     * @return true if valid (0-150), false otherwise
     */
    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 150;
    }
    
    /**
     * Validate gender selection
     * @param gender gender to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return false;
        }
        String cleanGender = gender.trim().toLowerCase();
        return cleanGender.equals("male") || cleanGender.equals("female") || 
               cleanGender.equals("other") || cleanGender.equals("prefer not to say");
    }
    
    /**
     * Validate blood type format
     * @param bloodType blood type to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidBloodType(String bloodType) {
        if (bloodType == null || bloodType.trim().isEmpty()) {
            return false;
        }
        String cleanBloodType = bloodType.trim().toUpperCase();
        return cleanBloodType.matches("^(A|B|AB|O)[+-]$");
    }
    
    /**
     * Validate date format (yyyy-MM-dd)
     * @param dateString date string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(dateString.trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Validate that date is not in the past
     * @param dateString date string to validate
     * @return true if date is today or in the future, false otherwise
     */
    public static boolean isFutureDate(String dateString) {
        if (!isValidDate(dateString)) {
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(dateString.trim(), DATE_FORMATTER);
            return !date.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Validate time slot format (HH:MM-HH:MM)
     * @param timeSlot time slot to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidTimeSlot(String timeSlot) {
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            return false;
        }
        return timeSlot.trim().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]-([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    }
    
    /**
     * Validate that input is not empty
     * @param input input to validate
     * @return true if not empty, false otherwise
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    /**
     * Validate that input is a positive integer
     * @param input input to validate
     * @return true if valid positive integer, false otherwise
     */
    public static boolean isValidPositiveInteger(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            int value = Integer.parseInt(input.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate that input is a valid integer
     * @param input input to validate
     * @return true if valid integer, false otherwise
     */
    public static boolean isValidInteger(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate appointment reason length
     * @param reason reason to validate
     * @return true if valid length (10-500 characters), false otherwise
     */
    public static boolean isValidAppointmentReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            return false;
        }
        String cleanReason = reason.trim();
        return cleanReason.length() >= 10 && cleanReason.length() <= 500;
    }
    
    /**
     * Validate medical diagnosis length
     * @param diagnosis diagnosis to validate
     * @return true if valid length (5-1000 characters), false otherwise
     */
    public static boolean isValidDiagnosis(String diagnosis) {
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            return false;
        }
        String cleanDiagnosis = diagnosis.trim();
        return cleanDiagnosis.length() >= 5 && cleanDiagnosis.length() <= 1000;
    }
    
    /**
     * Validate prescription length
     * @param prescription prescription to validate
     * @return true if valid length (5-500 characters), false otherwise
     */
    public static boolean isValidPrescription(String prescription) {
        if (prescription == null || prescription.trim().isEmpty()) {
            return false;
        }
        String cleanPrescription = prescription.trim();
        return cleanPrescription.length() >= 5 && cleanPrescription.length() <= 500;
    }
    
    /**
     * Sanitize input by trimming whitespace
     * @param input input to sanitize
     * @return sanitized input
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }
    
    /**
     * Validate user role selection
     * @param role role to validate
     * @return true if valid role, false otherwise
     */
    public static boolean isValidUserRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return false;
        }
        String cleanRole = role.trim().toUpperCase();
        return cleanRole.equals("ADMIN") || cleanRole.equals("DOCTOR") || cleanRole.equals("PATIENT");
    }
    
    /**
     * Validate appointment status
     * @param status status to validate
     * @return true if valid status, false otherwise
     */
    public static boolean isValidAppointmentStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        String cleanStatus = status.trim().toUpperCase();
        return cleanStatus.equals("SCHEDULED") || cleanStatus.equals("COMPLETED") || 
               cleanStatus.equals("CANCELLED") || cleanStatus.equals("RESCHEDULED");
    }
    
    /**
     * Get validation error message for common validation failures
     * @param fieldName name of the field being validated
     * @param validationType type of validation that failed
     * @return error message
     */
    public static String getValidationErrorMessage(String fieldName, String validationType) {
        switch (validationType.toLowerCase()) {
            case "email":
                return fieldName + " must be a valid email address (e.g., user@example.com)";
            case "phone":
                return fieldName + " must be a valid phone number (10-15 digits)";
            case "username":
                return fieldName + " must be 3-20 characters long and contain only letters, numbers, and underscores";
            case "password":
                return fieldName + " must be at least 8 characters long and contain at least one lowercase letter, one uppercase letter, and one number";
            case "name":
                return fieldName + " must be 2-50 characters long and contain only letters, spaces, hyphens, and apostrophes";
            case "age":
                return fieldName + " must be between 0 and 150";
            case "gender":
                return fieldName + " must be one of: Male, Female, Other, Prefer not to say";
            case "bloodtype":
                return fieldName + " must be a valid blood type (e.g., A+, B-, AB+, O-)";
            case "date":
                return fieldName + " must be a valid date in yyyy-MM-dd format";
            case "futuredate":
                return fieldName + " must be today or a future date";
            case "timeslot":
                return fieldName + " must be in HH:MM-HH:MM format";
            case "notempty":
                return fieldName + " cannot be empty";
            case "positiveinteger":
                return fieldName + " must be a positive integer";
            case "integer":
                return fieldName + " must be a valid integer";
            case "appointmentreason":
                return fieldName + " must be 10-500 characters long";
            case "diagnosis":
                return fieldName + " must be 5-1000 characters long";
            case "prescription":
                return fieldName + " must be 5-500 characters long";
            case "userrole":
                return fieldName + " must be one of: Admin, Doctor, Patient";
            case "appointmentstatus":
                return fieldName + " must be one of: Scheduled, Completed, Cancelled, Rescheduled";
            default:
                return fieldName + " is invalid";
        }
    }
}
