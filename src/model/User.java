package model;

/**
 * Abstract base class for all user types in the medical appointment system.
 * Implements common user attributes and behaviors using encapsulation.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public abstract class User {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String contactNumber;
    private String address;
    private boolean isActive;
    
    /**
     * Constructor for User class
     * @param username unique identifier for the user
     * @param password user's password
     * @param fullName complete name of the user
     * @param email user's email address
     * @param contactNumber user's phone number
     * @param address user's physical address
     */
    public User(String username, String password, String fullName, String email, 
                String contactNumber, String address) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.isActive = true;
    }
    
    // Getters and Setters (Encapsulation)
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    
    /**
     * Abstract method to display user information
     * Each subclass must implement this method
     */
    public abstract void displayInfo();
    
    /**
     * Abstract method to get user role
     * @return String representation of user role
     */
    public abstract String getUserRole();
    
    /**
     * Validates user credentials
     * @param inputUsername username to validate
     * @param inputPassword password to validate
     * @return true if credentials match, false otherwise
     */
    public boolean validateCredentials(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }
    
    /**
     * Returns formatted string representation of user
     * @return String containing user information
     */
    @Override
    public String toString() {
        return String.format("User: %s | Role: %s | Email: %s | Contact: %s", 
                           fullName, getUserRole(), email, contactNumber);
    }
}
