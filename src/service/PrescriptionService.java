package service;

import model.*;
import util.DataStore;
import util.InputValidator;
import util.ConsolePrinter;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * PrescriptionService class for handling prescription-related operations.
 * Implements business logic for prescription creation, management, and medication handling.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class PrescriptionService {
    private final DataStore dataStore;
    
    /**
     * Constructor for PrescriptionService
     * @param dataStore data store instance
     */
    public PrescriptionService(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
    /**
     * Create a new prescription for a patient
     * @param patientId ID of the patient
     * @param doctorId ID of the doctor creating the prescription
     * @param instructions general instructions for the prescription
     * @param validUntil date until which prescription is valid
     * @param refillsRemaining number of refills remaining
     * @return Prescription object if creation successful, null otherwise
     */
    public Prescription createPrescription(String patientId, String doctorId, String instructions, 
                                         LocalDate validUntil, int refillsRemaining) {
        
        // Validate inputs
        if (!validatePrescriptionInputs(patientId, doctorId, instructions, validUntil)) {
            return null;
        }
        
        // Check if patient exists
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            ConsolePrinter.printError("Patient not found");
            return null;
        }
        
        // Check if doctor exists
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            ConsolePrinter.printError("Doctor not found");
            return null;
        }
        
        // Generate unique prescription ID
        String prescriptionId = dataStore.generatePrescriptionId();
        
        // Create new prescription
        Prescription prescription = new Prescription(prescriptionId, patientId, doctorId, 
                                                  validUntil, refillsRemaining);
        prescription.setInstructions(instructions);
        
        // Add to data store
        if (dataStore.addPrescription(prescription)) {
            ConsolePrinter.printSuccess("Prescription created successfully!");
            ConsolePrinter.printInfo("Prescription ID: " + prescriptionId);
            ConsolePrinter.printInfo("Patient: " + patient.getFullName());
            ConsolePrinter.printInfo("Doctor: " + doctor.getFullName());
            ConsolePrinter.printInfo("Valid until: " + validUntil);
            return prescription;
        } else {
            ConsolePrinter.printError("Failed to save prescription");
            return null;
        }
    }
    
    /**
     * Add medication to an existing prescription
     * @param prescriptionId ID of the prescription
     * @param medicationName name of the medication
     * @param dosage dosage of the medication
     * @param frequency frequency of taking the medication
     * @param duration duration of the medication
     * @param instructions special instructions for the medication
     * @return true if medication added successfully, false otherwise
     */
    public boolean addMedicationToPrescription(String prescriptionId, String medicationName, 
                                            String dosage, String frequency, String duration, 
                                            String instructions) {
        
        // Validate inputs
        if (!validateMedicationInputs(medicationName, dosage, frequency, duration)) {
            return false;
        }
        
        Prescription prescription = dataStore.getPrescription(prescriptionId);
        if (prescription == null) {
            ConsolePrinter.printError("Prescription not found");
            return false;
        }
        
        prescription.addMedication(medicationName, dosage, frequency, duration, instructions);
        ConsolePrinter.printSuccess("Medication added to prescription successfully");
        ConsolePrinter.printInfo("Medication: " + medicationName);
        ConsolePrinter.printInfo("Dosage: " + dosage);
        ConsolePrinter.printInfo("Frequency: " + frequency);
        return true;
    }
    
    /**
     * Get prescriptions for a patient
     * @param patientId ID of the patient
     * @return list of prescriptions for the patient
     */
    public List<Prescription> getPrescriptionsForPatient(String patientId) {
        return dataStore.getPrescriptionsByPatient(patientId);
    }
    
    /**
     * Get prescriptions created by a doctor
     * @param doctorId ID of the doctor
     * @return list of prescriptions created by the doctor
     */
    public List<Prescription> getPrescriptionsByDoctor(String doctorId) {
        return dataStore.getPrescriptionsByDoctor(doctorId);
    }
    
    /**
     * Get all prescriptions
     * @return list of all prescriptions
     */
    public List<Prescription> getAllPrescriptions() {
        return new ArrayList<>(dataStore.getAllPrescriptions());
    }
    
    /**
     * Get active prescriptions for a patient
     * @param patientId ID of the patient
     * @return list of active prescriptions for the patient
     */
    public List<Prescription> getActivePrescriptionsForPatient(String patientId) {
        LocalDate today = LocalDate.now();
        return dataStore.getPrescriptionsByPatient(patientId).stream()
            .filter(prescription -> prescription.getStatus().equals("ACTIVE"))
            .filter(prescription -> prescription.getValidUntil() == null || prescription.getValidUntil().isAfter(today))
            .collect(Collectors.toList());
    }
    
    /**
     * Display prescriptions in a formatted table
     * @param prescriptions list of prescriptions to display
     * @param title title for the display
     */
    public void displayPrescriptions(List<Prescription> prescriptions, String title) {
        if (prescriptions.isEmpty()) {
            ConsolePrinter.printWarning("No prescriptions found");
            return;
        }
        
        ConsolePrinter.printSubHeader(title);
        
        String[] headers = {"ID", "Patient", "Doctor", "Status", "Valid Until", "Refills", "Medications"};
        List<String[]> data = new ArrayList<>();
        
        for (Prescription prescription : prescriptions) {
            Patient patient = findPatientById(prescription.getPatientId());
            Doctor doctor = findDoctorById(prescription.getDoctorId());
            
            String patientName = patient != null ? patient.getFullName() : "Unknown";
            String doctorName = doctor != null ? doctor.getFullName() : "Unknown";
            String validUntil = prescription.getValidUntil() != null ? prescription.getValidUntil().toString() : "N/A";
            String medicationCount = String.valueOf(prescription.getMedications().size());
            
            data.add(new String[]{
                prescription.getPrescriptionId(),
                patientName,
                doctorName,
                prescription.getStatus(),
                validUntil,
                String.valueOf(prescription.getRefillsRemaining()),
                medicationCount
            });
        }
        
        ConsolePrinter.printTable(headers, data);
    }
    
    /**
     * Display prescription details
     * @param prescriptionId ID of the prescription
     */
    public void displayPrescriptionDetails(String prescriptionId) {
        Prescription prescription = dataStore.getPrescription(prescriptionId);
        if (prescription == null) {
            ConsolePrinter.printError("Prescription not found");
            return;
        }
        
        Patient patient = findPatientById(prescription.getPatientId());
        Doctor doctor = findDoctorById(prescription.getDoctorId());
        
        ConsolePrinter.printSubHeader("Prescription Details");
        ConsolePrinter.printInfo("Prescription ID: " + prescription.getPrescriptionId());
        ConsolePrinter.printInfo("Status: " + prescription.getStatus());
        ConsolePrinter.printInfo("Valid Until: " + (prescription.getValidUntil() != null ? prescription.getValidUntil().toString() : "N/A"));
        ConsolePrinter.printInfo("Refills Remaining: " + prescription.getRefillsRemaining());
        ConsolePrinter.printInfo("Instructions: " + prescription.getInstructions());
        
        if (patient != null) {
            ConsolePrinter.printInfo("Patient: " + patient.getFullName());
        }
        
        if (doctor != null) {
            ConsolePrinter.printInfo("Doctor: " + doctor.getFullName());
        }
        
        // Display medications
        if (!prescription.getMedications().isEmpty()) {
            ConsolePrinter.printSubHeader("Medications");
            for (Prescription.Medication medication : prescription.getMedications()) {
                ConsolePrinter.printInfo("• " + medication.getMedicationName() + " - " + medication.getDosage());
                ConsolePrinter.printInfo("  Frequency: " + medication.getFrequency());
                ConsolePrinter.printInfo("  Duration: " + medication.getDuration());
                if (!medication.getInstructions().isEmpty()) {
                    ConsolePrinter.printInfo("  Instructions: " + medication.getInstructions());
                }
            }
        }
    }
    
    /**
     * Validate prescription inputs
     * @param patientId patient ID to validate
     * @param doctorId doctor ID to validate
     * @param instructions instructions to validate
     * @param validUntil valid until date to validate
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validatePrescriptionInputs(String patientId, String doctorId, String instructions, LocalDate validUntil) {
        boolean isValid = true;
        
        if (!InputValidator.isNotEmpty(patientId)) {
            ConsolePrinter.printError("Patient ID cannot be empty");
            isValid = false;
        }
        
        if (!InputValidator.isNotEmpty(doctorId)) {
            ConsolePrinter.printError("Doctor ID cannot be empty");
            isValid = false;
        }
        
        if (!InputValidator.isNotEmpty(instructions)) {
            ConsolePrinter.printError("Instructions cannot be empty");
            isValid = false;
        }
        
        if (validUntil != null && validUntil.isBefore(LocalDate.now())) {
            ConsolePrinter.printError("Valid until date cannot be in the past");
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Validate medication inputs
     * @param medicationName medication name to validate
     * @param dosage dosage to validate
     * @param frequency frequency to validate
     * @param duration duration to validate
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateMedicationInputs(String medicationName, String dosage, String frequency, String duration) {
        boolean isValid = true;
        
        if (!InputValidator.isNotEmpty(medicationName)) {
            ConsolePrinter.printError("Medication name cannot be empty");
            isValid = false;
        }
        
        if (!InputValidator.isNotEmpty(dosage)) {
            ConsolePrinter.printError("Dosage cannot be empty");
            isValid = false;
        }
        
        if (!InputValidator.isNotEmpty(frequency)) {
            ConsolePrinter.printError("Frequency cannot be empty");
            isValid = false;
        }
        
        if (!InputValidator.isNotEmpty(duration)) {
            ConsolePrinter.printError("Duration cannot be empty");
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Find patient by ID
     * @param patientId patient ID to search for
     * @return Patient object or null if not found
     */
    private Patient findPatientById(String patientId) {
        for (Patient patient : dataStore.getAllPatients()) {
            if (patient.getPatientId().equals(patientId)) {
                return patient;
            }
        }
        return null;
    }
    
    /**
     * Find doctor by ID
     * @param doctorId doctor ID to search for
     * @return Doctor object or null if not found
     */
    private Doctor findDoctorById(String doctorId) {
        for (Doctor doctor : dataStore.getAllDoctors()) {
            if (doctor.getDoctorId().equals(doctorId)) {
                return doctor;
            }
        }
        return null;
    }
    
    /**
     * Get prescription statistics
     * @return map containing prescription statistics
     */
    public java.util.Map<String, Object> getPrescriptionStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        List<Prescription> allPrescriptions = getAllPrescriptions();
        
        stats.put("totalPrescriptions", allPrescriptions.size());
        stats.put("activePrescriptions", allPrescriptions.stream()
            .filter(p -> p.getStatus().equals("ACTIVE"))
            .count());
        stats.put("expiredPrescriptions", allPrescriptions.stream()
            .filter(p -> p.getValidUntil() != null && p.getValidUntil().isBefore(LocalDate.now()))
            .count());
        
        return stats;
    }
    
    /**
     * Display prescription statistics
     */
    public void displayPrescriptionStatistics() {
        java.util.Map<String, Object> stats = getPrescriptionStatistics();
        ConsolePrinter.printSubHeader("Prescription Statistics");
        ConsolePrinter.printInfo("Total Prescriptions: " + stats.get("totalPrescriptions"));
        ConsolePrinter.printInfo("Active: " + stats.get("activePrescriptions"));
        ConsolePrinter.printInfo("Expired: " + stats.get("expiredPrescriptions"));
    }
}
