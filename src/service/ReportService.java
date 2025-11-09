package service;

import model.*;
import util.DataStore;
import util.ConsolePrinter;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * ReportService class for generating various reports and statistics.
 * Implements business logic for report generation and data analysis.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class ReportService {
    private final DataStore dataStore;
    
    /**
     * Constructor for ReportService
     * @param dataStore data store instance
     */
    public ReportService(DataStore dataStore) {
        this.dataStore = dataStore;
    }
    
    /**
     * Generate comprehensive system report
     * @return formatted system report
     */
    public String generateSystemReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("=== MEDICONNECT+ SYSTEM REPORT ===\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        // User Statistics
        report.append("USER STATISTICS:\n");
        report.append("-".repeat(20)).append("\n");
        report.append("Total Users: ").append(dataStore.getAllUsers().size()).append("\n");
        report.append("Admins: ").append(dataStore.getAllAdmins().size()).append("\n");
        report.append("Doctors: ").append(dataStore.getAllDoctors().size()).append("\n");
        report.append("Patients: ").append(dataStore.getAllPatients().size()).append("\n\n");
        
        // Appointment Statistics
        report.append("APPOINTMENT STATISTICS:\n");
        report.append("-".repeat(25)).append("\n");
        Map<String, Integer> appointmentStats = getAppointmentStatistics();
        for (Map.Entry<String, Integer> entry : appointmentStats.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("\n");
        
        // Medical Record Statistics
        report.append("MEDICAL RECORD STATISTICS:\n");
        report.append("-".repeat(28)).append("\n");
        report.append("Total Records: ").append(dataStore.getAllMedicalRecords().size()).append("\n");
        report.append("Active Records: ").append(getActiveMedicalRecords().size()).append("\n");
        report.append("Archived Records: ").append(getArchivedMedicalRecords().size()).append("\n\n");
        
        // Prescription Statistics
        report.append("PRESCRIPTION STATISTICS:\n");
        report.append("-".repeat(26)).append("\n");
        report.append("Total Prescriptions: ").append(dataStore.getAllPrescriptions().size()).append("\n");
        report.append("Active Prescriptions: ").append(getActivePrescriptions().size()).append("\n");
        report.append("Expired Prescriptions: ").append(getExpiredPrescriptions().size()).append("\n\n");
        
        return report.toString();
    }
    
    /**
     * Generate appointment report by date range
     * @param startDate start date for the report
     * @param endDate end date for the report
     * @return formatted appointment report
     */
    public String generateAppointmentReport(LocalDate startDate, LocalDate endDate) {
        StringBuilder report = new StringBuilder();
        
        report.append("=== APPOINTMENT REPORT ===\n");
        report.append("Date Range: ").append(startDate).append(" to ").append(endDate).append("\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        List<Appointment> appointmentsInRange = getAppointmentsByDateRange(startDate, endDate);
        
        report.append("APPOINTMENTS IN DATE RANGE:\n");
        report.append("-".repeat(30)).append("\n");
        report.append("Total Appointments: ").append(appointmentsInRange.size()).append("\n\n");
        
        // Group by status
        Map<String, Long> statusCounts = appointmentsInRange.stream()
            .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));
        
        report.append("APPOINTMENTS BY STATUS:\n");
        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("\n");
        
        // Group by doctor
        Map<String, Long> doctorCounts = appointmentsInRange.stream()
            .collect(Collectors.groupingBy(appointment -> {
                Doctor doctor = findDoctorById(appointment.getDoctorId());
                return doctor != null ? doctor.getFullName() : "Unknown Doctor";
            }, Collectors.counting()));
        
        report.append("APPOINTMENTS BY DOCTOR:\n");
        for (Map.Entry<String, Long> entry : doctorCounts.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("\n");
        
        // Detailed appointment list
        report.append("DETAILED APPOINTMENT LIST:\n");
        report.append("-".repeat(30)).append("\n");
        for (Appointment appointment : appointmentsInRange) {
            Doctor doctor = findDoctorById(appointment.getDoctorId());
            Patient patient = findPatientById(appointment.getPatientId());
            
            String doctorName = doctor != null ? doctor.getFullName() : "Unknown";
            String patientName = patient != null ? patient.getFullName() : "Unknown";
            
            report.append("ID: ").append(appointment.getAppointmentId())
                  .append(" | Date: ").append(appointment.getDate())
                  .append(" | Time: ").append(appointment.getTimeSlot())
                  .append(" | Doctor: ").append(doctorName)
                  .append(" | Patient: ").append(patientName)
                  .append(" | Status: ").append(appointment.getStatus())
                  .append(" | Reason: ").append(appointment.getReason())
                  .append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Generate doctor performance report
     * @param doctorId ID of the doctor
     * @return formatted doctor performance report
     */
    public String generateDoctorPerformanceReport(String doctorId) {
        StringBuilder report = new StringBuilder();
        
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            return "Doctor not found";
        }
        
        report.append("=== DOCTOR PERFORMANCE REPORT ===\n");
        report.append("Doctor: ").append(doctor.getFullName()).append("\n");
        report.append("Specialization: ").append(doctor.getSpecialization()).append("\n");
        report.append("Experience: ").append(doctor.getExperienceYears()).append(" years\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        List<Appointment> doctorAppointments = dataStore.getAppointmentsByDoctor(doctorId);
        
        report.append("APPOINTMENT STATISTICS:\n");
        report.append("-".repeat(25)).append("\n");
        report.append("Total Appointments: ").append(doctorAppointments.size()).append("\n");
        
        // Count by status
        Map<String, Long> statusCounts = doctorAppointments.stream()
            .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));
        
        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("\n");
        
        // Medical records created
        List<MedicalRecord> doctorRecords = dataStore.getMedicalRecordsByDoctor(doctorId);
        report.append("MEDICAL RECORDS CREATED:\n");
        report.append("-".repeat(28)).append("\n");
        report.append("Total Records: ").append(doctorRecords.size()).append("\n");
        report.append("Active Records: ").append(doctorRecords.stream()
            .filter(record -> record.getStatus().equals("ACTIVE"))
            .count()).append("\n");
        report.append("\n");
        
        // Prescriptions created
        List<Prescription> doctorPrescriptions = dataStore.getPrescriptionsByDoctor(doctorId);
        report.append("PRESCRIPTIONS CREATED:\n");
        report.append("-".repeat(25)).append("\n");
        report.append("Total Prescriptions: ").append(doctorPrescriptions.size()).append("\n");
        report.append("Active Prescriptions: ").append(doctorPrescriptions.stream()
            .filter(prescription -> prescription.getStatus().equals("ACTIVE"))
            .count()).append("\n");
        report.append("\n");
        
        return report.toString();
    }
    
    /**
     * Generate patient history report
     * @param patientId ID of the patient
     * @return formatted patient history report
     */
    public String generatePatientHistoryReport(String patientId) {
        StringBuilder report = new StringBuilder();
        
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            return "Patient not found";
        }
        
        report.append("=== PATIENT HISTORY REPORT ===\n");
        report.append("Patient: ").append(patient.getFullName()).append("\n");
        report.append("Patient ID: ").append(patient.getPatientId()).append("\n");
        report.append("Age: ").append(patient.getAge()).append("\n");
        report.append("Gender: ").append(patient.getGender()).append("\n");
        report.append("Blood Type: ").append(patient.getBloodType()).append("\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        // Appointment history
        List<Appointment> patientAppointments = dataStore.getAppointmentsByPatient(patientId);
        report.append("APPOINTMENT HISTORY:\n");
        report.append("-".repeat(20)).append("\n");
        report.append("Total Appointments: ").append(patientAppointments.size()).append("\n");
        
        Map<String, Long> statusCounts = patientAppointments.stream()
            .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));
        
        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        report.append("\n");
        
        // Medical records
        List<MedicalRecord> patientRecords = dataStore.getMedicalRecordsByPatient(patientId);
        report.append("MEDICAL RECORDS:\n");
        report.append("-".repeat(17)).append("\n");
        report.append("Total Records: ").append(patientRecords.size()).append("\n\n");
        
        for (MedicalRecord record : patientRecords) {
            Doctor doctor = findDoctorById(record.getDoctorId());
            String doctorName = doctor != null ? doctor.getFullName() : "Unknown";
            
            report.append("Record ID: ").append(record.getRecordId())
                  .append(" | Date: ").append(record.getRecordDate())
                  .append(" | Doctor: ").append(doctorName)
                  .append(" | Diagnosis: ").append(record.getDiagnosis())
                  .append(" | Status: ").append(record.getStatus())
                  .append("\n");
        }
        report.append("\n");
        
        // Prescriptions
        List<Prescription> patientPrescriptions = dataStore.getPrescriptionsByPatient(patientId);
        report.append("PRESCRIPTIONS:\n");
        report.append("-".repeat(14)).append("\n");
        report.append("Total Prescriptions: ").append(patientPrescriptions.size()).append("\n\n");
        
        for (Prescription prescription : patientPrescriptions) {
            Doctor doctor = findDoctorById(prescription.getDoctorId());
            String doctorName = doctor != null ? doctor.getFullName() : "Unknown";
            
            report.append("Prescription ID: ").append(prescription.getPrescriptionId())
                  .append(" | Date: ").append(prescription.getPrescriptionDate())
                  .append(" | Doctor: ").append(doctorName)
                  .append(" | Status: ").append(prescription.getStatus())
                  .append(" | Valid Until: ").append(prescription.getValidUntil())
                  .append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Get appointments by date range
     * @param startDate start date
     * @param endDate end date
     * @return list of appointments in the date range
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return dataStore.getAllAppointments().stream()
            .filter(appointment -> !appointment.getDate().isBefore(startDate) && 
                                 !appointment.getDate().isAfter(endDate))
            .collect(Collectors.toList());
    }
    
    /**
     * Get appointment statistics
     * @return map containing appointment statistics
     */
    public Map<String, Integer> getAppointmentStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        List<Appointment> allAppointments = new ArrayList<>(dataStore.getAllAppointments());
        
        stats.put("Total", allAppointments.size());
        stats.put("Scheduled", (int) allAppointments.stream()
            .filter(appointment -> appointment.getStatus().equals("SCHEDULED"))
            .count());
        stats.put("Completed", (int) allAppointments.stream()
            .filter(appointment -> appointment.getStatus().equals("COMPLETED"))
            .count());
        stats.put("Cancelled", (int) allAppointments.stream()
            .filter(appointment -> appointment.getStatus().equals("CANCELLED"))
            .count());
        stats.put("Rescheduled", (int) allAppointments.stream()
            .filter(appointment -> appointment.getStatus().equals("RESCHEDULED"))
            .count());
        
        return stats;
    }
    
    /**
     * Get active medical records
     * @return list of active medical records
     */
    public List<MedicalRecord> getActiveMedicalRecords() {
        return dataStore.getAllMedicalRecords().stream()
            .filter(record -> record.getStatus().equals("ACTIVE"))
            .collect(Collectors.toList());
    }
    
    /**
     * Get archived medical records
     * @return list of archived medical records
     */
    public List<MedicalRecord> getArchivedMedicalRecords() {
        return dataStore.getAllMedicalRecords().stream()
            .filter(record -> record.getStatus().equals("ARCHIVED"))
            .collect(Collectors.toList());
    }
    
    /**
     * Get active prescriptions
     * @return list of active prescriptions
     */
    public List<Prescription> getActivePrescriptions() {
        return dataStore.getAllPrescriptions().stream()
            .filter(prescription -> prescription.getStatus().equals("ACTIVE"))
            .collect(Collectors.toList());
    }
    
    /**
     * Get expired prescriptions
     * @return list of expired prescriptions
     */
    public List<Prescription> getExpiredPrescriptions() {
        return dataStore.getAllPrescriptions().stream()
            .filter(Prescription::isExpired)
            .collect(Collectors.toList());
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
     * Display system report
     */
    public void displaySystemReport() {
        ConsolePrinter.printSubHeader("System Report");
        String report = generateSystemReport();
        System.out.println(report);
    }
    
    /**
     * Display appointment report for date range
     * @param startDate start date
     * @param endDate end date
     */
    public void displayAppointmentReport(LocalDate startDate, LocalDate endDate) {
        ConsolePrinter.printSubHeader("Appointment Report (" + startDate + " to " + endDate + ")");
        String report = generateAppointmentReport(startDate, endDate);
        System.out.println(report);
    }
    
    /**
     * Display doctor performance report
     * @param doctorId doctor ID
     */
    public void displayDoctorPerformanceReport(String doctorId) {
        ConsolePrinter.printSubHeader("Doctor Performance Report");
        String report = generateDoctorPerformanceReport(doctorId);
        System.out.println(report);
    }
    
    /**
     * Display patient history report
     * @param patientId patient ID
     */
    public void displayPatientHistoryReport(String patientId) {
        ConsolePrinter.printSubHeader("Patient History Report");
        String report = generatePatientHistoryReport(patientId);
        System.out.println(report);
    }
    
    /**
     * Export report to file (simulated)
     * @param report report content
     * @param filename filename for the report
     * @return true if export successful, false otherwise
     */
    public boolean exportReportToFile(String report, String filename) {
        ConsolePrinter.printInfo("Exporting report to " + filename + "...");
        ConsolePrinter.printLoading("Generating file");
        ConsolePrinter.printSuccess("Report exported successfully to " + filename);
        ConsolePrinter.printWarning("Note: This is a demo application. In a real system, the file would be saved to disk.");
        return true;
    }
}
