package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Admin class representing system administrators.
 * Inherits from User and implements admin-specific functionality.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class Admin extends User {
    private String adminId;
    private List<String> permissions;
    
    /**
     * Constructor for Admin class
     * @param username unique identifier for the admin
     * @param password admin's password
     * @param fullName complete name of the admin
     * @param email admin's email address
     * @param contactNumber admin's phone number
     * @param address admin's physical address
     * @param adminId unique admin identifier
     */
    public Admin(String username, String password, String fullName, String email, 
                 String contactNumber, String address, String adminId) {
        super(username, password, fullName, email, contactNumber, address);
        this.adminId = adminId;
        this.permissions = new ArrayList<>();
        initializePermissions();
    }
    
    /**
     * Initialize default admin permissions
     */
    private void initializePermissions() {
        permissions.add("MANAGE_DOCTORS");
        permissions.add("MANAGE_PATIENTS");
        permissions.add("VIEW_ALL_APPOINTMENTS");
        permissions.add("GENERATE_REPORTS");
        permissions.add("SYSTEM_ADMINISTRATION");
    }
    
    /**
     * Get admin ID
     * @return admin ID
     */
    public String getAdminId() { return adminId; }
    
    /**
     * Set admin ID
     * @param adminId new admin ID
     */
    public void setAdminId(String adminId) { this.adminId = adminId; }
    
    /**
     * Get list of permissions
     * @return list of permissions
     */
    public List<String> getPermissions() { return permissions; }
    
    /**
     * Add a new permission
     * @param permission permission to add
     */
    public void addPermission(String permission) {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }
    
    /**
     * Remove a permission
     * @param permission permission to remove
     */
    public void removePermission(String permission) {
        permissions.remove(permission);
    }
    
    /**
     * Check if admin has specific permission
     * @param permission permission to check
     * @return true if admin has permission, false otherwise
     */
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
    
    /**
     * Implementation of abstract method from User class
     * Displays admin information
     */
    @Override
    public void displayInfo() {
        System.out.println("=== ADMIN INFORMATION ===");
        System.out.println("Admin ID: " + adminId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Contact: " + getContactNumber());
        System.out.println("Address: " + getAddress());
        System.out.println("Permissions: " + permissions.toString());
        System.out.println("Status: " + (isActive() ? "Active" : "Inactive"));
    }
    
    /**
     * Implementation of abstract method from User class
     * @return user role as String
     */
    @Override
    public String getUserRole() {
        return "ADMIN";
    }
    
    /**
     * Add a new doctor to the system
     * @param doctor doctor object to add
     * @return true if successful, false otherwise
     */
    public boolean addDoctor(Doctor doctor) {
        if (hasPermission("MANAGE_DOCTORS")) {
            // Implementation would add doctor to data store
            System.out.println("Doctor " + doctor.getFullName() + " added successfully.");
            return true;
        }
        System.out.println("Insufficient permissions to add doctor.");
        return false;
    }
    
    /**
     * Remove a doctor from the system
     * @param doctorId ID of doctor to remove
     * @return true if successful, false otherwise
     */
    public boolean removeDoctor(String doctorId) {
        if (hasPermission("MANAGE_DOCTORS")) {
            // Implementation would remove doctor from data store
            System.out.println("Doctor with ID " + doctorId + " removed successfully.");
            return true;
        }
        System.out.println("Insufficient permissions to remove doctor.");
        return false;
    }
    
    /**
     * View all appointments in the system
     */
    public void viewAllAppointments() {
        if (hasPermission("VIEW_ALL_APPOINTMENTS")) {
            System.out.println("=== ALL APPOINTMENTS ===");
            // Implementation would fetch and display all appointments
            System.out.println("Displaying all appointments...");
        } else {
            System.out.println("Insufficient permissions to view all appointments.");
        }
    }
    
    /**
     * Generate system reports
     */
    public void generateReport() {
        if (hasPermission("GENERATE_REPORTS")) {
            System.out.println("=== GENERATING SYSTEM REPORT ===");
            // Implementation would generate comprehensive reports
            System.out.println("Report generated successfully.");
        } else {
            System.out.println("Insufficient permissions to generate reports.");
        }
    }
}
