package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * MedicalRecord class representing patient medical records.
 * Implements encapsulation and provides medical record management functionality.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class MedicalRecord {
    private String recordId;
    private String patientId;
    private String doctorId;
    private String diagnosis;
    private String prescription;
    private String treatment;
    private String notes;
    private LocalDate recordDate;
    private LocalDateTime createdDateTime;
    private String status;
    private List<String> symptoms;
    private List<String> medications;
    private String followUpRequired;
    private LocalDate followUpDate;
    
    /**
     * Constructor for MedicalRecord class
     * @param recordId unique record identifier
     * @param patientId ID of the patient
     * @param doctorId ID of the doctor
     * @param diagnosis medical diagnosis
     * @param prescription prescribed medications
     */
    public MedicalRecord(String recordId, String patientId, String doctorId, 
                         String diagnosis, String prescription) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.treatment = "";
        this.notes = "";
        this.recordDate = LocalDate.now();
        this.createdDateTime = LocalDateTime.now();
        this.status = "ACTIVE";
        this.symptoms = new ArrayList<>();
        this.medications = new ArrayList<>();
        this.followUpRequired = "NO";
        this.followUpDate = null;
    }
    
    // Getters and Setters (Encapsulation)
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }
    
    public LocalDateTime getCreatedDateTime() { return createdDateTime; }
    public void setCreatedDateTime(LocalDateTime createdDateTime) { this.createdDateTime = createdDateTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<String> getSymptoms() { return symptoms; }
    public void setSymptoms(List<String> symptoms) { this.symptoms = symptoms; }
    
    public List<String> getMedications() { return medications; }
    public void setMedications(List<String> medications) { this.medications = medications; }
    
    public String getFollowUpRequired() { return followUpRequired; }
    public void setFollowUpRequired(String followUpRequired) { this.followUpRequired = followUpRequired; }
    
    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
    
    /**
     * Add a symptom
     * @param symptom symptom to add
     */
    public void addSymptom(String symptom) {
        if (!symptoms.contains(symptom)) {
            symptoms.add(symptom);
        }
    }
    
    /**
     * Remove a symptom
     * @param symptom symptom to remove
     */
    public void removeSymptom(String symptom) {
        symptoms.remove(symptom);
    }
    
    /**
     * Add a medication
     * @param medication medication to add
     */
    public void addMedication(String medication) {
        if (!medications.contains(medication)) {
            medications.add(medication);
        }
    }
    
    /**
     * Remove a medication
     * @param medication medication to remove
     */
    public void removeMedication(String medication) {
        medications.remove(medication);
    }
    
    /**
     * Create a new medical record
     * @return true if successful, false otherwise
     */
    public boolean createRecord() {
        if (diagnosis.isEmpty() || prescription.isEmpty()) {
            System.out.println("Cannot create record without diagnosis and prescription.");
            return false;
        }
        this.status = "ACTIVE";
        System.out.println("Medical record " + recordId + " created successfully.");
        return true;
    }
    
    /**
     * Update the medical record
     * @param newDiagnosis updated diagnosis
     * @param newPrescription updated prescription
     * @param newTreatment updated treatment
     * @param newNotes additional notes
     * @return true if successful, false otherwise
     */
    public boolean updateRecord(String newDiagnosis, String newPrescription, 
                               String newTreatment, String newNotes) {
        if (status.equals("ARCHIVED")) {
            System.out.println("Cannot update an archived record.");
            return false;
        }
        
        if (!newDiagnosis.isEmpty()) {
            this.diagnosis = newDiagnosis;
        }
        if (!newPrescription.isEmpty()) {
            this.prescription = newPrescription;
        }
        if (!newTreatment.isEmpty()) {
            this.treatment = newTreatment;
        }
        if (!newNotes.isEmpty()) {
            this.notes = newNotes;
        }
        
        System.out.println("Medical record " + recordId + " updated successfully.");
        return true;
    }
    
    /**
     * Archive the medical record
     * @return true if successful, false otherwise
     */
    public boolean archiveRecord() {
        if (status.equals("ARCHIVED")) {
            System.out.println("Record " + recordId + " is already archived.");
            return false;
        }
        this.status = "ARCHIVED";
        System.out.println("Medical record " + recordId + " archived successfully.");
        return true;
    }
    
    /**
     * Set follow-up requirement
     * @param required whether follow-up is required
     * @param followUpDate follow-up date if required
     */
    public void setFollowUp(boolean required, LocalDate followUpDate) {
        this.followUpRequired = required ? "YES" : "NO";
        this.followUpDate = followUpDate;
        System.out.println("Follow-up " + (required ? "scheduled" : "cancelled") + " for record " + recordId);
    }
    
    /**
     * Check if follow-up is due
     * @return true if follow-up is due, false otherwise
     */
    public boolean isFollowUpDue() {
        if (!followUpRequired.equals("YES") || followUpDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(followUpDate) || LocalDate.now().equals(followUpDate);
    }
    
    /**
     * Display the medical record
     */
    public void displayRecord() {
        System.out.println("=== MEDICAL RECORD ===");
        System.out.println("Record ID: " + recordId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Date: " + recordDate);
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("Prescription: " + prescription);
        System.out.println("Treatment: " + treatment);
        System.out.println("Notes: " + notes);
        System.out.println("Symptoms: " + symptoms.toString());
        System.out.println("Medications: " + medications.toString());
        System.out.println("Status: " + status);
        System.out.println("Follow-up Required: " + followUpRequired);
        if (followUpDate != null) {
            System.out.println("Follow-up Date: " + followUpDate);
        }
        System.out.println("Created: " + createdDateTime);
    }
    
    /**
     * Get formatted record summary
     * @return String containing record summary
     */
    public String getRecordSummary() {
        return String.format("ID: %s | Date: %s | Diagnosis: %s | Status: %s", 
                           recordId, recordDate, diagnosis, status);
    }
    
    /**
     * Returns formatted string representation of medical record
     * @return String containing medical record information
     */
    @Override
    public String toString() {
        return String.format("MedicalRecord[ID: %s, Patient: %s, Doctor: %s, Date: %s, Diagnosis: %s]", 
                           recordId, patientId, doctorId, recordDate, diagnosis);
    }
}
