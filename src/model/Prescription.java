package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Prescription class representing medical prescriptions.
 * Implements encapsulation and provides prescription management functionality.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class Prescription {
    private String prescriptionId;
    private String patientId;
    private String doctorId;
    private LocalDate prescriptionDate;
    private LocalDateTime createdDateTime;
    private List<Medication> medications;
    private String instructions;
    private String status;
    private LocalDate validUntil;
    private int refillsRemaining;
    private String notes;
    
    /**
     * Inner class representing individual medications in a prescription
     */
    public static class Medication {
        private String medicationName;
        private String dosage;
        private String frequency;
        private String duration;
        private String instructions;
        
        /**
         * Constructor for Medication
         * @param medicationName name of the medication
         * @param dosage dosage amount
         * @param frequency how often to take
         * @param duration how long to take
         * @param instructions specific instructions
         */
        public Medication(String medicationName, String dosage, String frequency, 
                         String duration, String instructions) {
            this.medicationName = medicationName;
            this.dosage = dosage;
            this.frequency = frequency;
            this.duration = duration;
            this.instructions = instructions;
        }
        
        // Getters and Setters
        public String getMedicationName() { return medicationName; }
        public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
        
        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        
        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }
        
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        
        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
        
        @Override
        public String toString() {
            return String.format("%s - %s %s %s for %s", medicationName, dosage, frequency, instructions, duration);
        }
    }
    
    /**
     * Constructor for Prescription class
     * @param prescriptionId unique prescription identifier
     * @param patientId ID of the patient
     * @param doctorId ID of the doctor
     * @param validUntil date until which prescription is valid
     * @param refillsRemaining number of refills allowed
     */
    public Prescription(String prescriptionId, String patientId, String doctorId, 
                        LocalDate validUntil, int refillsRemaining) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.prescriptionDate = LocalDate.now();
        this.createdDateTime = LocalDateTime.now();
        this.medications = new ArrayList<>();
        this.instructions = "";
        this.status = "ACTIVE";
        this.validUntil = validUntil;
        this.refillsRemaining = refillsRemaining;
        this.notes = "";
    }
    
    // Getters and Setters (Encapsulation)
    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }
    
    public LocalDateTime getCreatedDateTime() { return createdDateTime; }
    public void setCreatedDateTime(LocalDateTime createdDateTime) { this.createdDateTime = createdDateTime; }
    
    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    
    public int getRefillsRemaining() { return refillsRemaining; }
    public void setRefillsRemaining(int refillsRemaining) { this.refillsRemaining = refillsRemaining; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    /**
     * Add a medication to the prescription
     * @param medication medication to add
     */
    public void addMedication(Medication medication) {
        medications.add(medication);
        System.out.println("Medication " + medication.getMedicationName() + " added to prescription " + prescriptionId);
    }
    
    /**
     * Add a medication with parameters
     * @param medicationName name of the medication
     * @param dosage dosage amount
     * @param frequency how often to take
     * @param duration how long to take
     * @param instructions specific instructions
     */
    public void addMedication(String medicationName, String dosage, String frequency, 
                             String duration, String instructions) {
        Medication medication = new Medication(medicationName, dosage, frequency, duration, instructions);
        addMedication(medication);
    }
    
    /**
     * Remove a medication from the prescription
     * @param medicationName name of the medication to remove
     * @return true if successful, false otherwise
     */
    public boolean removeMedication(String medicationName) {
        for (Medication medication : medications) {
            if (medication.getMedicationName().equals(medicationName)) {
                medications.remove(medication);
                System.out.println("Medication " + medicationName + " removed from prescription " + prescriptionId);
                return true;
            }
        }
        System.out.println("Medication " + medicationName + " not found in prescription " + prescriptionId);
        return false;
    }
    
    /**
     * Update prescription instructions
     * @param newInstructions new instructions
     */
    public void updateInstructions(String newInstructions) {
        this.instructions = newInstructions;
        System.out.println("Instructions updated for prescription " + prescriptionId);
    }
    
    /**
     * Add notes to the prescription
     * @param newNotes notes to add
     */
    public void addNotes(String newNotes) {
        if (notes.isEmpty()) {
            notes = newNotes;
        } else {
            notes += "\n" + newNotes;
        }
        System.out.println("Notes added to prescription " + prescriptionId);
    }
    
    /**
     * Process a refill request
     * @return true if refill is allowed, false otherwise
     */
    public boolean processRefill() {
        if (refillsRemaining <= 0) {
            System.out.println("No refills remaining for prescription " + prescriptionId);
            return false;
        }
        if (LocalDate.now().isAfter(validUntil)) {
            System.out.println("Prescription " + prescriptionId + " has expired");
            return false;
        }
        if (!status.equals("ACTIVE")) {
            System.out.println("Prescription " + prescriptionId + " is not active");
            return false;
        }
        
        refillsRemaining--;
        System.out.println("Refill processed for prescription " + prescriptionId + ". Refills remaining: " + refillsRemaining);
        return true;
    }
    
    /**
     * Check if prescription is valid
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return status.equals("ACTIVE") && !LocalDate.now().isAfter(validUntil);
    }
    
    /**
     * Check if prescription is expired
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(validUntil);
    }
    
    /**
     * Cancel the prescription
     * @return true if successful, false otherwise
     */
    public boolean cancel() {
        if (status.equals("CANCELLED")) {
            System.out.println("Prescription " + prescriptionId + " is already cancelled");
            return false;
        }
        this.status = "CANCELLED";
        System.out.println("Prescription " + prescriptionId + " cancelled successfully");
        return true;
    }
    
    /**
     * Display the prescription
     */
    public void displayPrescription() {
        System.out.println("=== PRESCRIPTION ===");
        System.out.println("Prescription ID: " + prescriptionId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Date: " + prescriptionDate);
        System.out.println("Valid Until: " + validUntil);
        System.out.println("Refills Remaining: " + refillsRemaining);
        System.out.println("Status: " + status);
        System.out.println("Instructions: " + instructions);
        System.out.println("Notes: " + notes);
        System.out.println("\nMedications:");
        for (int i = 0; i < medications.size(); i++) {
            System.out.println((i + 1) + ". " + medications.get(i).toString());
        }
    }
    
    /**
     * Get formatted prescription summary
     * @return String containing prescription summary
     */
    public String getPrescriptionSummary() {
        return String.format("ID: %s | Date: %s | Valid Until: %s | Refills: %d | Status: %s", 
                           prescriptionId, prescriptionDate, validUntil, refillsRemaining, status);
    }
    
    /**
     * Returns formatted string representation of prescription
     * @return String containing prescription information
     */
    @Override
    public String toString() {
        return String.format("Prescription[ID: %s, Patient: %s, Doctor: %s, Date: %s, Status: %s]", 
                           prescriptionId, patientId, doctorId, prescriptionDate, status);
    }
}
