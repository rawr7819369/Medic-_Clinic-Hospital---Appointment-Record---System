package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Doctor class representing medical professionals.
 * Inherits from User and implements doctor-specific functionality.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class Doctor extends User {
    private String doctorId;
    private String specialization;
    private String licenseNumber;
    private List<String> qualifications;
    private List<String> availableTimeSlots;
    private int experienceYears;
    
    /**
     * Constructor for Doctor class
     * @param username unique identifier for the doctor
     * @param password doctor's password
     * @param fullName complete name of the doctor
     * @param email doctor's email address
     * @param contactNumber doctor's phone number
     * @param address doctor's physical address
     * @param doctorId unique doctor identifier
     * @param specialization medical specialization
     * @param licenseNumber medical license number
     * @param experienceYears years of experience
     */
    public Doctor(String username, String password, String fullName, String email, 
                  String contactNumber, String address, String doctorId, 
                  String specialization, String licenseNumber, int experienceYears) {
        super(username, password, fullName, email, contactNumber, address);
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.experienceYears = experienceYears;
        this.qualifications = new ArrayList<>();
        this.availableTimeSlots = new ArrayList<>();
        initializeDefaultTimeSlots();
    }
    
    /**
     * Initialize default available time slots
     */
    private void initializeDefaultTimeSlots() {
        availableTimeSlots.add("09:00-10:00");
        availableTimeSlots.add("10:00-11:00");
        availableTimeSlots.add("11:00-12:00");
        availableTimeSlots.add("14:00-15:00");
        availableTimeSlots.add("15:00-16:00");
        availableTimeSlots.add("16:00-17:00");
    }
    
    // Getters and Setters
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public List<String> getQualifications() { return qualifications; }
    public void setQualifications(List<String> qualifications) { this.qualifications = qualifications; }
    
    public List<String> getAvailableTimeSlots() { return availableTimeSlots; }
    public void setAvailableTimeSlots(List<String> availableTimeSlots) { this.availableTimeSlots = availableTimeSlots; }
    
    /**
     * Add a qualification
     * @param qualification qualification to add
     */
    public void addQualification(String qualification) {
        if (!qualifications.contains(qualification)) {
            qualifications.add(qualification);
        }
    }
    
    /**
     * Remove a qualification
     * @param qualification qualification to remove
     */
    public void removeQualification(String qualification) {
        qualifications.remove(qualification);
    }
    
    /**
     * Add available time slot
     * @param timeSlot time slot to add
     */
    public void addTimeSlot(String timeSlot) {
        if (!availableTimeSlots.contains(timeSlot)) {
            availableTimeSlots.add(timeSlot);
        }
    }
    
    /**
     * Remove time slot
     * @param timeSlot time slot to remove
     */
    public void removeTimeSlot(String timeSlot) {
        availableTimeSlots.remove(timeSlot);
    }
    
    /**
     * Check if doctor is available at specific time
     * @param timeSlot time slot to check
     * @return true if available, false otherwise
     */
    public boolean isAvailableAt(String timeSlot) {
        return availableTimeSlots.contains(timeSlot);
    }
    
    /**
     * Implementation of abstract method from User class
     * Displays doctor information
     */
    @Override
    public void displayInfo() {
        System.out.println("=== DOCTOR INFORMATION ===");
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Name: " + getFullName());
        System.out.println("Specialization: " + specialization);
        System.out.println("License Number: " + licenseNumber);
        System.out.println("Experience: " + experienceYears + " years");
        System.out.println("Email: " + getEmail());
        System.out.println("Contact: " + getContactNumber());
        System.out.println("Address: " + getAddress());
        System.out.println("Qualifications: " + qualifications.toString());
        System.out.println("Available Time Slots: " + availableTimeSlots.toString());
        System.out.println("Status: " + (isActive() ? "Active" : "Inactive"));
    }
    
    /**
     * Implementation of abstract method from User class
     * @return user role as String
     */
    @Override
    public String getUserRole() {
        return "DOCTOR";
    }
    
    /**
     * View doctor's appointments
     */
    public void viewAppointments() {
        System.out.println("=== DOCTOR'S APPOINTMENTS ===");
        System.out.println("Displaying appointments for Dr. " + getFullName());
        // Implementation would fetch and display doctor's appointments
    }
    
    /**
     * Add medical record for a patient
     * @param patientId ID of the patient
     * @param diagnosis diagnosis made
     * @param prescription prescribed medication
     * @param notes additional notes
     * @return true if successful, false otherwise
     */
    public boolean addMedicalRecord(String patientId, String diagnosis, String prescription, String notes) {
        System.out.println("=== ADDING MEDICAL RECORD ===");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("Prescription: " + prescription);
        System.out.println("Notes: " + notes);
        System.out.println("Medical record added successfully by Dr. " + getFullName());
        return true;
    }
    
    /**
     * View patient's medical record
     * @param patientId ID of the patient
     */
    public void viewPatientRecord(String patientId) {
        System.out.println("=== PATIENT MEDICAL RECORD ===");
        System.out.println("Viewing medical record for Patient ID: " + patientId);
        // Implementation would fetch and display patient's medical record
    }
    
    /**
     * Create prescription for a patient
     * @param patientId ID of the patient
     * @param medication prescribed medication
     * @param dosage dosage information
     * @param instructions usage instructions
     * @return true if successful, false otherwise
     */
    public boolean createPrescription(String patientId, String medication, String dosage, String instructions) {
        System.out.println("=== CREATING PRESCRIPTION ===");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Medication: " + medication);
        System.out.println("Dosage: " + dosage);
        System.out.println("Instructions: " + instructions);
        System.out.println("Prescription created by Dr. " + getFullName());
        return true;
    }
}
