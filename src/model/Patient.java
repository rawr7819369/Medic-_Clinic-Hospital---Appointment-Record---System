package model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Patient class representing patients in the medical system.
 * Inherits from User and implements patient-specific functionality.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class Patient extends User {
    private String patientId;
    private int age;
    private String gender;
    private String bloodType;
    private String emergencyContact;
    private String medicalHistory;
    private List<String> allergies;
    private List<String> currentMedications;
    private LocalDate registrationDate;
    
    /**
     * Constructor for Patient class
     * @param username unique identifier for the patient
     * @param password patient's password
     * @param fullName complete name of the patient
     * @param email patient's email address
     * @param contactNumber patient's phone number
     * @param address patient's physical address
     * @param patientId unique patient identifier
     * @param age patient's age
     * @param gender patient's gender
     * @param bloodType patient's blood type
     * @param emergencyContact emergency contact information
     */
    public Patient(String username, String password, String fullName, String email, 
                   String contactNumber, String address, String patientId, 
                   int age, String gender, String bloodType, String emergencyContact) {
        super(username, password, fullName, email, contactNumber, address);
        this.patientId = patientId;
        this.age = age;
        this.gender = gender;
        this.bloodType = bloodType;
        this.emergencyContact = emergencyContact;
        this.medicalHistory = "";
        this.allergies = new ArrayList<>();
        this.currentMedications = new ArrayList<>();
        this.registrationDate = LocalDate.now();
    }
    
    // Getters and Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    
    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }
    
    public List<String> getCurrentMedications() { return currentMedications; }
    public void setCurrentMedications(List<String> currentMedications) { this.currentMedications = currentMedications; }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    
    /**
     * Add an allergy
     * @param allergy allergy to add
     */
    public void addAllergy(String allergy) {
        if (!allergies.contains(allergy)) {
            allergies.add(allergy);
        }
    }
    
    /**
     * Remove an allergy
     * @param allergy allergy to remove
     */
    public void removeAllergy(String allergy) {
        allergies.remove(allergy);
    }
    
    /**
     * Add current medication
     * @param medication medication to add
     */
    public void addCurrentMedication(String medication) {
        if (!currentMedications.contains(medication)) {
            currentMedications.add(medication);
        }
    }
    
    /**
     * Remove current medication
     * @param medication medication to remove
     */
    public void removeCurrentMedication(String medication) {
        currentMedications.remove(medication);
    }
    
    /**
     * Update medical history
     * @param newEntry new medical history entry
     */
    public void updateMedicalHistory(String newEntry) {
        if (medicalHistory.isEmpty()) {
            medicalHistory = newEntry;
        } else {
            medicalHistory += "\n" + newEntry;
        }
    }
    
    /**
     * Implementation of abstract method from User class
     * Displays patient information
     */
    @Override
    public void displayInfo() {
        System.out.println("=== PATIENT INFORMATION ===");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Name: " + getFullName());
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Email: " + getEmail());
        System.out.println("Contact: " + getContactNumber());
        System.out.println("Address: " + getAddress());
        System.out.println("Emergency Contact: " + emergencyContact);
        System.out.println("Registration Date: " + registrationDate);
        System.out.println("Allergies: " + allergies.toString());
        System.out.println("Current Medications: " + currentMedications.toString());
        System.out.println("Status: " + (isActive() ? "Active" : "Inactive"));
    }
    
    /**
     * Implementation of abstract method from User class
     * @return user role as String
     */
    @Override
    public String getUserRole() {
        return "PATIENT";
    }
    
    /**
     * Book an appointment
     * @param doctorId ID of the doctor
     * @param date appointment date
     * @param timeSlot appointment time slot
     * @param reason reason for appointment
     * @return true if successful, false otherwise
     */
    public boolean bookAppointment(String doctorId, String date, String timeSlot, String reason) {
        System.out.println("=== BOOKING APPOINTMENT ===");
        System.out.println("Patient: " + getFullName() + " (ID: " + patientId + ")");
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Date: " + date);
        System.out.println("Time: " + timeSlot);
        System.out.println("Reason: " + reason);
        System.out.println("Appointment booked successfully!");
        return true;
    }
    
    /**
     * View appointment history
     */
    public void viewAppointmentHistory() {
        System.out.println("=== APPOINTMENT HISTORY ===");
        System.out.println("Displaying appointment history for " + getFullName());
        // Implementation would fetch and display patient's appointment history
    }
    
    /**
     * View medical history
     */
    public void viewMedicalHistory() {
        System.out.println("=== MEDICAL HISTORY ===");
        System.out.println("Patient: " + getFullName() + " (ID: " + patientId + ")");
        System.out.println("Medical History: " + medicalHistory);
        System.out.println("Allergies: " + allergies.toString());
        System.out.println("Current Medications: " + currentMedications.toString());
    }
    
    /**
     * Update profile information
     * @param newEmail new email address
     * @param newContact new contact number
     * @param newAddress new address
     * @return true if successful, false otherwise
     */
    public boolean updateProfile(String newEmail, String newContact, String newAddress) {
        setEmail(newEmail);
        setContactNumber(newContact);
        setAddress(newAddress);
        System.out.println("Profile updated successfully for " + getFullName());
        return true;
    }
}
