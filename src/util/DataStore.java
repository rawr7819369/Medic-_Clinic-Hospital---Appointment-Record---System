package util;

import model.*;
import service.DatabaseService;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DataStore utility class for managing in-memory data storage.
 * Implements singleton pattern to ensure single instance across the application.
 * Provides thread-safe operations for data management.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class DataStore {
    private static DataStore instance;
    private final Map<String, User> users;
    private final Map<String, Admin> admins;
    private final Map<String, Doctor> doctors;
    private final Map<String, Patient> patients;
    private final Map<String, Appointment> appointments;
    private final Map<String, MedicalRecord> medicalRecords;
    private final Map<String, Prescription> prescriptions;
    private final Map<String, String> userCredentials; // username -> password
    private final Map<String, String> userRoles; // username -> role
    private final DatabaseService databaseService;
    private boolean useDatabase = true; // Flag to enable/disable database usage
    
    // Private constructor for singleton pattern
    private DataStore() {
        this.users = new ConcurrentHashMap<>();
        this.admins = new ConcurrentHashMap<>();
        this.doctors = new ConcurrentHashMap<>();
        this.patients = new ConcurrentHashMap<>();
        this.appointments = new ConcurrentHashMap<>();
        this.medicalRecords = new ConcurrentHashMap<>();
        this.prescriptions = new ConcurrentHashMap<>();
        this.userCredentials = new ConcurrentHashMap<>();
        this.userRoles = new ConcurrentHashMap<>();
        this.databaseService = new DatabaseService();
        
        // Initialize with default data for fallback
        initializeDefaultData();
        
        // Ensure database has the same defaults (no-ops if already present)
        try {
            databaseService.seedDefaultsIfMissing();
        } catch (Exception ignore) {}
        
        // Load data from database if available
        if (useDatabase) {
            loadDataFromDatabase();
        }
    }
    
    /**
     * Get singleton instance of DataStore
     * @return DataStore instance
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
    
    /**
     * Load data from database into memory
     */
    private void loadDataFromDatabase() {
        try {
            // Load admins from database
            List<Admin> dbAdmins = databaseService.getAllAdmins();
            for (Admin admin : dbAdmins) {
                admins.put(admin.getUsername(), admin);
                users.put(admin.getUsername(), admin);
                userCredentials.put(admin.getUsername(), admin.getPassword());
                userRoles.put(admin.getUsername(), "ADMIN");
            }
            
            // Load doctors from database
            List<Doctor> dbDoctors = databaseService.getAllDoctors();
            for (Doctor doctor : dbDoctors) {
                doctors.put(doctor.getUsername(), doctor);
                users.put(doctor.getUsername(), doctor);
                userCredentials.put(doctor.getUsername(), doctor.getPassword());
                userRoles.put(doctor.getUsername(), "DOCTOR");
            }
            
            // Load patients from database
            List<Patient> dbPatients = databaseService.getAllPatients();
            for (Patient patient : dbPatients) {
                patients.put(patient.getUsername(), patient);
                users.put(patient.getUsername(), patient);
                userCredentials.put(patient.getUsername(), patient.getPassword());
                userRoles.put(patient.getUsername(), "PATIENT");
            }
            
            // Load appointments from database
            List<Appointment> dbAppointments = databaseService.getAllAppointments();
            for (Appointment ap : dbAppointments) {
                appointments.put(ap.getAppointmentId(), ap);
            }
            
            System.out.println("Data loaded from database successfully!");
        } catch (Exception e) {
            System.err.println("Error loading data from database: " + e.getMessage());
            System.out.println("Using in-memory data only.");
        }
    }
    
    /**
     * Initialize default data for testing and demonstration
     */
    private void initializeDefaultData() {
        // Create default admin
        Admin defaultAdmin = new Admin("admin", "Admin123!", "System Administrator", 
                                     "admin@mediconnect.com", "1234567890", "123 Admin St", "ADM001");
        addUser(defaultAdmin);
        
        // Create multiple default doctors to ensure availability
        Doctor defaultDoctor1 = new Doctor("doctor", "Doctor123!", "Dr. John Smith", 
                                         "doctor@mediconnect.com", "0987654321", "456 Doctor Ave", 
                                         "DOC001", "General Medicine", "LIC001", 10);
        addUser(defaultDoctor1);
        
        Doctor defaultDoctor2 = new Doctor("doctor2", "Doctor123!", "Dr. Sarah Johnson", 
                                         "doctor2@mediconnect.com", "0987654322", "457 Doctor Ave", 
                                         "DOC002", "Cardiology", "LIC002", 8);
        addUser(defaultDoctor2);
        
        Doctor defaultDoctor3 = new Doctor("doctor3", "Doctor123!", "Dr. Michael Brown", 
                                         "doctor3@mediconnect.com", "0987654323", "458 Doctor Ave", 
                                         "DOC003", "Dermatology", "LIC003", 12);
        addUser(defaultDoctor3);
        
        // Create default patient
        Patient defaultPatient = new Patient("patient", "Patient123!", "Jane Doe", 
                                          "patient@mediconnect.com", "1122334455", "789 Patient Blvd", 
                                          "PAT001", 30, "Female", "A+", "9998887777");
        addUser(defaultPatient);
        
        // Create sample appointments with different doctors to ensure availability
        createSampleAppointments();
        createSampleMedicalRecords();
        createSamplePrescriptions();
    }
    
    /**
     * Create sample appointments for demonstration
     */
    private void createSampleAppointments() {
        // Create appointments with different doctors to ensure availability
        Appointment appointment1 = new Appointment("APT001", "DOC001", "PAT001", 
                                                  LocalDate.now().plusDays(1), LocalTime.of(9, 0), 
                                                  "09:00-10:00", "Regular checkup");
        appointment1.book(); // This will set status to SCHEDULED
        // Save via normal pathway so it persists to DB if enabled
        addAppointment(appointment1);
        
        Appointment appointment2 = new Appointment("APT002", "DOC002", "PAT001", 
                                                  LocalDate.now().plusDays(3), LocalTime.of(14, 0), 
                                                  "14:00-15:00", "Follow-up consultation");
        appointment2.book(); // This will set status to SCHEDULED
        addAppointment(appointment2);
        
        // Add a few more sample appointments with DOC003 to show variety
        Appointment appointment3 = new Appointment("APT003", "DOC003", "PAT001", 
                                                  LocalDate.now().plusDays(2), LocalTime.of(10, 0), 
                                                  "10:00-11:00", "Dermatology consultation");
        appointment3.book(); // This will set status to SCHEDULED
        addAppointment(appointment3);
    }
    
    /**
     * Create sample medical records for demonstration
     */
    private void createSampleMedicalRecords() {
        MedicalRecord record1 = new MedicalRecord("REC001", "PAT001", "DOC001", 
                                                 "Hypertension", "Lisinopril 10mg daily");
        record1.createRecord();
        // Persist to DB if available
        try { databaseService.saveMedicalRecord(record1); } catch (Exception ignore) {}
        medicalRecords.put("REC001", record1);
        
        MedicalRecord record2 = new MedicalRecord("REC002", "PAT001", "DOC001", 
                                                 "Diabetes Type 2", "Metformin 500mg twice daily");
        record2.createRecord();
        try { databaseService.saveMedicalRecord(record2); } catch (Exception ignore) {}
        medicalRecords.put("REC002", record2);
    }
    
    /**
     * Create sample prescriptions for demonstration
     */
    private void createSamplePrescriptions() {
        Prescription prescription1 = new Prescription("PRES001", "PAT001", "DOC001", 
                                                      LocalDate.now().plusMonths(3), 2);
        prescription1.addMedication("Lisinopril", "10mg", "Once daily", "30 days", "Take with food");
        prescription1.addMedication("Metformin", "500mg", "Twice daily", "30 days", "Take with meals");
        // Use addPrescription to save to DB if available
        addPrescription(prescription1);
    }
    
    // User Management Methods
    
    /**
     * Add a user to the data store
     * @param user user to add
     * @return true if successful, false otherwise
     */
    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        
        String username = user.getUsername();
        if (users.containsKey(username)) {
            return false; // User already exists
        }
        
        // Try to save to database first if enabled
        boolean dbSuccess = true;
        if (useDatabase) {
            if (user instanceof Doctor) {
                dbSuccess = databaseService.saveDoctor((Doctor) user);
            } else if (user instanceof Patient) {
                dbSuccess = databaseService.savePatient((Patient) user);
            } else if (user instanceof Admin) {
                dbSuccess = databaseService.saveAdmin((Admin) user);
            }
        }
        
        // If database save failed due to duplicate entry, still add to memory
        if (useDatabase && !dbSuccess) {
            System.err.println("Failed to save user to database: " + username);
            // Don't return false here - continue to add to memory for existing users
        }
        
        // Add to memory storage
        users.put(username, user);
        userCredentials.put(username, user.getPassword());
        userRoles.put(username, user.getUserRole());
        
        // Add to specific role map
        if (user instanceof Admin) {
            admins.put(username, (Admin) user);
        } else if (user instanceof Doctor) {
            doctors.put(username, (Doctor) user);
        } else if (user instanceof Patient) {
            patients.put(username, (Patient) user);
        }
        
        return true;
    }
    
    /**
     * Get user by username
     * @param username username to search for
     * @return User object or null if not found
     */
    public User getUser(String username) {
        return users.get(username);
    }
    
    /**
     * Get admin by username
     * @param username username to search for
     * @return Admin object or null if not found
     */
    public Admin getAdmin(String username) {
        return admins.get(username);
    }
    
    /**
     * Get doctor by username
     * @param username username to search for
     * @return Doctor object or null if not found
     */
    public Doctor getDoctor(String username) {
        return doctors.get(username);
    }
    
    /**
     * Get patient by username
     * @param username username to search for
     * @return Patient object or null if not found
     */
    public Patient getPatient(String username) {
        return patients.get(username);
    }
    
    /**
     * Validate user credentials
     * @param username username to validate
     * @param password password to validate
     * @return true if credentials are valid, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        String storedPassword = userCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
    
    /**
     * Get user role by username
     * @param username username to search for
     * @return user role or null if not found
     */
    public String getUserRole(String username) {
        return userRoles.get(username);
    }
    
    /**
     * Check if user exists by username
     * @param username username to check
     * @return true if user exists, false otherwise
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
    
    /**
     * Get all users
     * @return collection of all users
     */
    public Collection<User> getAllUsers() {
        return users.values();
    }
    
    /**
     * Get all admins
     * @return collection of all admins
     */
    public Collection<Admin> getAllAdmins() {
        return admins.values();
    }
    
    /**
     * Get all doctors
     * @return collection of all doctors
     */
    public Collection<Doctor> getAllDoctors() {
        /*
        if (useDatabase) {
            // Load fresh data from database
            List<Doctor> dbDoctors = databaseService.getAllDoctors();
            doctors.clear();
            for (Doctor doctor : dbDoctors) {
                doctors.put(doctor.getDoctorId(), doctor);
            }
        }
        */
        return doctors.values();
    }
    
    /**
     * Get all patients
     * @return collection of all patients
     */
    public Collection<Patient> getAllPatients() {
        /*
        if (useDatabase) {
            // Load fresh data from database
            List<Patient> dbPatients = databaseService.getAllPatients();
            patients.clear();
            for (Patient patient : dbPatients) {
                patients.put(patient.getPatientId(), patient);
            }
        }
        */
        return patients.values();
    }

    // -------- Dynamic count helpers (DB-backed with in-memory fallback) --------
    public int countTotalUsers() {
        if (useDatabase) {
            return databaseService.countUsers();
        }
        return users.size();
    }

    public int countTotalDoctors() {
        if (useDatabase) {
            return databaseService.countDoctors();
        }
        return doctors.size();
    }

    public int countTotalPatients() {
        if (useDatabase) {
            return databaseService.countPatients();
        }
        return patients.size();
    }

    public int countTotalAppointments() {
        if (useDatabase) {
            return databaseService.countAppointments();
        }
        return appointments.size();
    }

    public int countAppointmentsByStatus(String status) {
        if (useDatabase) {
            return databaseService.countAppointmentsByStatus(status);
        }
        int cnt = 0;
        for (Appointment a : appointments.values()) {
            if (status != null && status.equalsIgnoreCase(a.getStatus())) {
                cnt++;
            }
        }
        return cnt;
    }

    public int countUpcomingAppointmentsByPatient(String patientId) {
        if (useDatabase) {
            return databaseService.countUpcomingAppointmentsByPatient(patientId);
        }
        java.time.LocalDate today = java.time.LocalDate.now();
        int cnt = 0;
        for (Appointment a : getAppointmentsByPatient(patientId)) {
            if ((a.getDate().isAfter(today) || a.getDate().equals(today)) &&
                !"CANCELLED".equalsIgnoreCase(a.getStatus())) {
                cnt++;
            }
        }
        return cnt;
    }

    public int countAppointmentsByPatient(String patientId) {
        if (useDatabase) {
            return databaseService.countAppointmentsByPatient(patientId);
        }
        return getAppointmentsByPatient(patientId).size();
    }

    public int countMedicalRecordsByPatient(String patientId) {
        if (useDatabase) {
            return databaseService.countMedicalRecordsByPatient(patientId);
        }
        return getMedicalRecordsByPatient(patientId).size();
    }

    public int countPrescriptionsByPatient(String patientId) {
        if (useDatabase) {
            return databaseService.countPrescriptionsByPatient(patientId);
        }
        return getPrescriptionsByPatient(patientId).size();
    }
    
    // Appointment Management Methods
    
    /**
     * Add appointment to data store
     * @param appointment appointment to add
     * @return true if successful, false otherwise
     */
    public boolean addAppointment(Appointment appointment) {
        if (appointment == null) {
            return false;
        }
        
        // Try to save to database first if enabled
        boolean dbSuccess = true;
        if (useDatabase) {
            dbSuccess = databaseService.saveAppointment(appointment);
        }
        
        // If database save failed, still add to in-memory store to preserve functionality
        if (useDatabase && !dbSuccess) {
            System.err.println("Failed to save appointment to database: " + appointment.getAppointmentId() + ". Adding to in-memory store.");
        }
        appointments.put(appointment.getAppointmentId(), appointment);
        return true;
    }
    
    /**
     * Get appointment by ID
     * @param appointmentId appointment ID to search for
     * @return Appointment object or null if not found
     */
    public Appointment getAppointment(String appointmentId) {
        return appointments.get(appointmentId);
    }
    
    /**
     * Get appointments by doctor ID
     * @param doctorId doctor ID to search for
     * @return list of appointments for the doctor
     */
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }
    
    /**
     * Get appointments by patient ID
     * @param patientId patient ID to search for
     * @return list of appointments for the patient
     */
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatientId().equals(patientId)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }
    
    /**
     * Get all appointments
     * @return collection of all appointments
     */
    public Collection<Appointment> getAllAppointments() {
        return appointments.values();
    }
    
    // Medical Record Management Methods
    
    /**
     * Add medical record to data store
     * @param medicalRecord medical record to add
     * @return true if successful, false otherwise
     */
    public boolean addMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return false;
        }
        medicalRecords.put(medicalRecord.getRecordId(), medicalRecord);
        return true;
    }
    
    /**
     * Get medical record by ID
     * @param recordId record ID to search for
     * @return MedicalRecord object or null if not found
     */
    public MedicalRecord getMedicalRecord(String recordId) {
        return medicalRecords.get(recordId);
    }
    
    /**
     * Get medical records by patient ID
     * @param patientId patient ID to search for
     * @return list of medical records for the patient
     */
    public List<MedicalRecord> getMedicalRecordsByPatient(String patientId) {
        List<MedicalRecord> patientRecords = new ArrayList<>();
        for (MedicalRecord record : medicalRecords.values()) {
            if (record.getPatientId().equals(patientId)) {
                patientRecords.add(record);
            }
        }
        return patientRecords;
    }
    
    /**
     * Get medical records by doctor ID
     * @param doctorId doctor ID to search for
     * @return list of medical records created by the doctor
     */
    public List<MedicalRecord> getMedicalRecordsByDoctor(String doctorId) {
        List<MedicalRecord> doctorRecords = new ArrayList<>();
        for (MedicalRecord record : medicalRecords.values()) {
            if (record.getDoctorId().equals(doctorId)) {
                doctorRecords.add(record);
            }
        }
        return doctorRecords;
    }
    
    /**
     * Get all medical records
     * @return collection of all medical records
     */
    public Collection<MedicalRecord> getAllMedicalRecords() {
        return medicalRecords.values();
    }
    
    // Prescription Management Methods
    
    /**
     * Add prescription to data store
     * @param prescription prescription to add
     * @return true if successful, false otherwise
     */
    public boolean addPrescription(Prescription prescription) {
        if (prescription == null) {
            return false;
        }
        
        // Try to save to database first if enabled
        boolean dbSuccess = true;
        if (useDatabase) {
            dbSuccess = databaseService.savePrescription(prescription);
        }
        
        // If database save failed, don't add to memory
        if (useDatabase && !dbSuccess) {
            System.err.println("Failed to save prescription to database: " + prescription.getPrescriptionId());
            return false;
        }
        
        prescriptions.put(prescription.getPrescriptionId(), prescription);
        return true;
    }
    
    /**
     * Get prescription by ID
     * @param prescriptionId prescription ID to search for
     * @return Prescription object or null if not found
     */
    public Prescription getPrescription(String prescriptionId) {
        return prescriptions.get(prescriptionId);
    }
    
    /**
     * Get prescriptions by patient ID
     * @param patientId patient ID to search for
     * @return list of prescriptions for the patient
     */
    public List<Prescription> getPrescriptionsByPatient(String patientId) {
        List<Prescription> patientPrescriptions = new ArrayList<>();
        for (Prescription prescription : prescriptions.values()) {
            if (prescription.getPatientId().equals(patientId)) {
                patientPrescriptions.add(prescription);
            }
        }
        return patientPrescriptions;
    }
    
    /**
     * Get prescriptions by doctor ID
     * @param doctorId doctor ID to search for
     * @return list of prescriptions created by the doctor
     */
    public List<Prescription> getPrescriptionsByDoctor(String doctorId) {
        List<Prescription> doctorPrescriptions = new ArrayList<>();
        for (Prescription prescription : prescriptions.values()) {
            if (prescription.getDoctorId().equals(doctorId)) {
                doctorPrescriptions.add(prescription);
            }
        }
        return doctorPrescriptions;
    }
    
    /**
     * Get all prescriptions
     * @return collection of all prescriptions
     */
    public Collection<Prescription> getAllPrescriptions() {
        return prescriptions.values();
    }
    
    // Scans Management Methods
    public boolean addScan(Scan scan) {
        if (scan == null) return false;
        if (useDatabase) {
            return databaseService.saveScan(scan);
        }
        return true; // in-memory mode: we don't store files, assume success
    }
    
    public java.util.List<Scan> getScansByPatient(String patientId) {
        if (useDatabase) {
            return databaseService.getScansByPatient(patientId);
        }
        return new ArrayList<>();
    }
    
    // Utility Methods
    
    /**
     * Generate unique ID for appointments
     * @return unique appointment ID
     */
    public String generateAppointmentId() {
        int counter = appointments.size() + 1;
        String appointmentId;
        
        // Ensure the generated ID is unique
        do {
            appointmentId = "APT" + String.format("%03d", counter);
            counter++;
        } while (appointments.containsKey(appointmentId));
        
        return appointmentId;
    }
    
    /**
     * Generate unique ID for medical records
     * @return unique medical record ID
     */
    public String generateMedicalRecordId() {
        return "REC" + String.format("%03d", medicalRecords.size() + 1);
    }
    
    /**
     * Generate unique ID for prescriptions
     * @return unique prescription ID
     */
    public String generatePrescriptionId() {
        return "PRES" + String.format("%03d", prescriptions.size() + 1);
    }
    
    public String generateScanId() {
        // Use timestamp-based to avoid conflicts
        return "SCAN" + System.currentTimeMillis();
    }
    
    /**
     * Check if appointment time slot is available
     * @param doctorId doctor ID
     * @param date appointment date
     * @param timeSlot time slot to check
     * @return true if available, false otherwise
     */
    public boolean isTimeSlotAvailable(String doctorId, LocalDate date, String timeSlot) {
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorId().equals(doctorId) && 
                appointment.getDate().equals(date) && 
                appointment.getTimeSlot().equals(timeSlot) &&
                !appointment.getStatus().equals("CANCELLED")) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get statistics for reports
     * @return map containing various statistics
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsers", users.size());
        stats.put("totalAdmins", admins.size());
        stats.put("totalDoctors", doctors.size());
        stats.put("totalPatients", patients.size());
        stats.put("totalAppointments", appointments.size());
        stats.put("totalMedicalRecords", medicalRecords.size());
        stats.put("totalPrescriptions", prescriptions.size());
        
        // Count appointments by status
        Map<String, Integer> appointmentStats = new HashMap<>();
        for (Appointment appointment : appointments.values()) {
            String status = appointment.getStatus();
            appointmentStats.put(status, appointmentStats.getOrDefault(status, 0) + 1);
        }
        stats.putAll(appointmentStats);
        
        return stats;
    }
    
    /**
     * Remove a doctor by their doctorId (not by username).
     * This will remove the doctor entry and all user-level entries (users map, credentials, roles).
     * @param doctorId the unique doctor identifier (e.g., "DOC001")
     * @return true if a doctor was found and removed; false otherwise
     */
    public boolean removeDoctorById(String doctorId) {
        if (doctorId == null || doctorId.isEmpty()) return false;
        String foundUsername = null;
        for (Map.Entry<String, Doctor> entry : doctors.entrySet()) {
            Doctor d = entry.getValue();
            if (d != null && doctorId.equals(d.getDoctorId())) {
                foundUsername = entry.getKey();
                break;
            }
        }
        if (foundUsername != null) {
            // remove from role-specific maps and users map and credentials/roles
            doctors.remove(foundUsername);
            users.remove(foundUsername);
            admins.remove(foundUsername);
            patients.remove(foundUsername);
            userCredentials.remove(foundUsername);
            userRoles.remove(foundUsername);
            // Optionally, remove related appointments/records — left to caller if needed
            return true;
        }
        return false;
    }
    
    /**
     * Clear all data (for testing purposes)
     */
    public void clearAllData() {
        users.clear();
        admins.clear();
        doctors.clear();
        patients.clear();
        appointments.clear();
        medicalRecords.clear();
        prescriptions.clear();
        userCredentials.clear();
        userRoles.clear();
    }
    
    /**
     * Get data store summary
     * @return string containing data store summary
     */
    public String getDataStoreSummary() {
        return String.format("DataStore Summary:\n" +
                           "Users: %d | Admins: %d | Doctors: %d | Patients: %d\n" +
                           "Appointments: %d | Medical Records: %d | Prescriptions: %d",
                           users.size(), admins.size(), doctors.size(), patients.size(),
                           appointments.size(), medicalRecords.size(), prescriptions.size());
    }
}
