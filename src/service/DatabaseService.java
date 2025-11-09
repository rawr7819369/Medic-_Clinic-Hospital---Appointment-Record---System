package service;

import model.*;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseService class for handling database operations.
 * Provides CRUD operations for all entities in the system.
 * 
 * @author MediConnect+ Development Team
 * @version 1.0
 */
public class DatabaseService {
    
    /**
     * Get total count from a table
     * @param table table name
     * @return total row count
     */
    private int countFromTable(String table) {
        String sql = "SELECT COUNT(*) AS cnt FROM " + table;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return 0;
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting from table " + table + ": " + e.getMessage());
        }
        return 0;
    }

    /**
     * Save a scan record to the database
     */
    public boolean saveScan(Scan scan) {
        String sql = "INSERT INTO scans (scan_id, patient_id, appointment_id, file_path, file_type, file_size, uploaded_at, description) VALUES (?,?,?,?,?,?,NOW(),?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, scan.getScanId());
            stmt.setString(2, scan.getPatientId());
            stmt.setString(3, scan.getAppointmentId());
            stmt.setString(4, scan.getFilePath());
            stmt.setString(5, scan.getFileType());
            stmt.setLong(6, scan.getFileSizeBytes());
            stmt.setString(7, scan.getDescription());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error saving scan: " + e.getMessage());
            return false;
        }
    }

    /**
     * Seed default accounts, doctors, patient, appointments, medical records, and prescriptions.
     * Uses INSERT IGNORE so it only inserts when missing and stays quiet if already present.
     */
    public void seedDefaultsIfMissing() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return;
            // Users
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO users (username, password, full_name, email, contact_number, address, is_active) VALUES "+
                "('admin','Admin123!','System Administrator','admin@mediconnect.com','1234567890','123 Admin St',1),"+
                "('doctor','Doctor123!','Dr. John Smith','doctor@mediconnect.com','0987654321','456 Doctor Ave',1),"+
                "('doctor2','Doctor123!','Dr. Sarah Johnson','doctor2@mediconnect.com','0987654322','457 Doctor Ave',1),"+
                "('doctor3','Doctor123!','Dr. Michael Brown','doctor3@mediconnect.com','0987654323','458 Doctor Ave',1),"+
                "('patient','Patient123!','Jane Doe','patient@mediconnect.com','1122334455','789 Patient Blvd',1)"
            )) { ps.executeUpdate(); }

            // Admins
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO admins (admin_id, username, permissions) VALUES ('ADM001','admin','manage_users,view_reports')"
            )) { ps.executeUpdate(); }

            // Doctors
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO doctors (doctor_id, username, specialization, license_number, experience_years, qualifications) VALUES "+
                "('DOC001','doctor','General Medicine','LIC001',10,''),"+
                "('DOC002','doctor2','Cardiology','LIC002',8,''),"+
                "('DOC003','doctor3','Dermatology','LIC003',12,'')"
            )) { ps.executeUpdate(); }

            // Patients
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO patients (patient_id, username, age, gender, blood_type, emergency_contact, medical_history, allergies, current_medications) VALUES "+
                "('PAT001','patient',30,'Female','A+','9998887777','','','')"
            )) { ps.executeUpdate(); }

            // Appointments
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO appointments (appointment_id, doctor_id, patient_id, appointment_date, appointment_time, time_slot, reason, status, notes) VALUES "+
                "('APT001','DOC001','PAT001',CURDATE()+INTERVAL 1 DAY, '09:00:00','09:00-10:00','Regular checkup','SCHEDULED',''),"+
                "('APT002','DOC002','PAT001',CURDATE()+INTERVAL 3 DAY, '14:00:00','14:00-15:00','Follow-up consultation','SCHEDULED',''),"+
                "('APT003','DOC003','PAT001',CURDATE()+INTERVAL 2 DAY, '10:00:00','10:00-11:00','Dermatology consultation','SCHEDULED','')"
            )) { ps.executeUpdate(); }

            // Medical records
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO medical_records (record_id, patient_id, doctor_id, diagnosis, prescription, treatment, notes, status, symptoms, medications) VALUES "+
                "('REC001','PAT001','DOC001','Hypertension','Lisinopril 10mg daily','','','','',''),"+
                "('REC002','PAT001','DOC001','Diabetes Type 2','Metformin 500mg twice daily','','','','','')"
            )) { ps.executeUpdate(); }

            // Prescription + medications
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO prescriptions (prescription_id, patient_id, doctor_id, instructions, status, valid_until, refills_remaining) VALUES "+
                "('PRES001','PAT001','DOC001','Take as directed','ACTIVE', CURDATE()+INTERVAL 90 DAY, 2)"
            )) { ps.executeUpdate(); }

            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO prescription_medications (prescription_id, medication_name, dosage, frequency, duration) VALUES "+
                "('PRES001','Lisinopril','10mg','Once daily','30 days'),"+
                "('PRES001','Metformin','500mg','Twice daily','30 days')"
            )) { ps.executeUpdate(); }
        } catch (SQLException e) {
            System.err.println("Error seeding defaults: " + e.getMessage());
        }
    }

    /**
     * Get scans by patient id
     */
    public java.util.List<Scan> getScansByPatient(String patientId) {
        java.util.List<Scan> list = new java.util.ArrayList<>();
        String sql = "SELECT scan_id, patient_id, appointment_id, file_path, file_type, file_size, uploaded_at, description FROM scans WHERE patient_id=? ORDER BY uploaded_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return list;
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Scan s = new Scan(
                        rs.getString("scan_id"),
                        rs.getString("patient_id"),
                        rs.getString("appointment_id"),
                        rs.getString("file_path"),
                        rs.getString("file_type"),
                        rs.getLong("file_size")
                    );
                    s.setDescription(rs.getString("description"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving scans by patient: " + e.getMessage());
        }
        return list;
    }

    /**
     * Get scans by appointment id
     */
    public java.util.List<Scan> getScansByAppointment(String appointmentId) {
        java.util.List<Scan> list = new java.util.ArrayList<>();
        String sql = "SELECT scan_id, patient_id, appointment_id, file_path, file_type, file_size, uploaded_at, description FROM scans WHERE appointment_id=? ORDER BY uploaded_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return list;
            stmt.setString(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Scan s = new Scan(
                        rs.getString("scan_id"),
                        rs.getString("patient_id"),
                        rs.getString("appointment_id"),
                        rs.getString("file_path"),
                        rs.getString("file_type"),
                        rs.getLong("file_size")
                    );
                    s.setDescription(rs.getString("description"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving scans by appointment: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieve all appointments from the database
     */
    public java.util.List<Appointment> getAllAppointments() {
        java.util.List<Appointment> list = new java.util.ArrayList<>();
        String sql = "SELECT appointment_id, doctor_id, patient_id, appointment_date, appointment_time, time_slot, reason, status, COALESCE(notes,'') AS notes FROM appointments";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return list;
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    model.Appointment a = new model.Appointment(
                        rs.getString("appointment_id"),
                        rs.getString("doctor_id"),
                        rs.getString("patient_id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("time_slot"),
                        rs.getString("reason")
                    );
                    a.setStatus(rs.getString("status"));
                    a.setNotes(rs.getString("notes"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
        }
        return list;
    }

    /**
     * Update appointment status (and optionally append a reason to notes)
     * @param appointmentId appointment id
     * @param newStatus new status value
     * @param reason optional reason to append to notes (nullable)
     * @return true if updated, false otherwise
     */
    public boolean updateAppointmentStatus(String appointmentId, String newStatus, String reason) {
        String sql = "UPDATE appointments SET status=?, notes=CONCAT(COALESCE(notes,''), ?) WHERE appointment_id=?";
        String notesAppend = (reason != null && !reason.isEmpty()) ? ("\nReason: " + reason) : "";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return false;
            stmt.setString(1, newStatus);
            stmt.setString(2, notesAppend);
            stmt.setString(3, appointmentId);
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get total users count
     */
    public int countUsers() { return countFromTable("users"); }

    /**
     * Update user's contact information (email, contact number, address)
     * @param username user's username
     * @param email new email
     * @param contact new contact number
     * @param address new address
     * @return true if update succeeded
     */
    public boolean updateUserContact(String username, String email, String contact, String address) {
        String sql = "UPDATE users SET email=?, contact_number=?, address=? WHERE username=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return false;
            stmt.setString(1, email);
            stmt.setString(2, contact);
            stmt.setString(3, address);
            stmt.setString(4, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user contact: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update user's password
     * @param username user's username
     * @param newPassword new password (plain for now)
     * @return true if updated
     */
    public boolean updateUserPassword(String username, String newPassword) {
        String sql = "UPDATE users SET password=? WHERE username=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return false;
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update patient allergies list (comma-separated) by patient_id
     */
    public boolean updatePatientAllergies(String patientId, java.util.List<String> allergies) {
        String sql = "UPDATE patients SET allergies=? WHERE patient_id=?";
        String csv = String.join(",", allergies != null ? allergies : java.util.Collections.emptyList());
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return false;
            stmt.setString(1, csv);
            stmt.setString(2, patientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating patient allergies: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update patient current medications list (comma-separated) by patient_id
     */
    public boolean updatePatientMedications(String patientId, java.util.List<String> meds) {
        String sql = "UPDATE patients SET current_medications=? WHERE patient_id=?";
        String csv = String.join(",", meds != null ? meds : java.util.Collections.emptyList());
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return false;
            stmt.setString(1, csv);
            stmt.setString(2, patientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating patient medications: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get total doctors count
     */
    public int countDoctors() { return countFromTable("doctors"); }

    /**
     * Get total patients count
     */
    public int countPatients() { return countFromTable("patients"); }

    /**
     * Get total appointments count
     */
    public int countAppointments() { return countFromTable("appointments"); }

    /**
     * Count appointments by status
     * @param status appointment status
     */
    public int countAppointmentsByStatus(String status) {
        String sql = "SELECT COUNT(*) AS cnt FROM appointments WHERE UPPER(status)=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return 0;
            stmt.setString(1, status != null ? status.toUpperCase() : "");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.err.println("Error counting appointments by status: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Count upcoming appointments for a patient (today or future, excluding cancelled)
     */
    public int countUpcomingAppointmentsByPatient(String patientId) {
        String sql = "SELECT COUNT(*) AS cnt FROM appointments WHERE patient_id=? AND appointment_date>=CURDATE() AND UPPER(status)<> 'CANCELLED'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return 0;
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.err.println("Error counting upcoming appointments by patient: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Count all appointments for a patient
     */
    public int countAppointmentsByPatient(String patientId) {
        String sql = "SELECT COUNT(*) AS cnt FROM appointments WHERE patient_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return 0;
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.err.println("Error counting appointments by patient: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Count medical records for a patient
     */
    public int countMedicalRecordsByPatient(String patientId) {
        String sql = "SELECT COUNT(*) AS cnt FROM medical_records WHERE patient_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return 0;
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.err.println("Error counting medical records by patient: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Count prescriptions for a patient
     */
    public int countPrescriptionsByPatient(String patientId) {
        String sql = "SELECT COUNT(*) AS cnt FROM prescriptions WHERE patient_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {
            if (stmt == null) return 0;
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.err.println("Error counting prescriptions by patient: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Save a patient to the database
     * @param patient patient to save
     * @return true if successful, false otherwise
     */
    public boolean savePatient(Patient patient) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                System.err.println("Database connection not available, skipping database save");
                return false;
            }
            conn.setAutoCommit(false);
            
            // Insert into users table
            String userSql = "INSERT INTO users (username, password, full_name, email, contact_number, address, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                userStmt.setString(1, patient.getUsername());
                userStmt.setString(2, patient.getPassword());
                userStmt.setString(3, patient.getFullName());
                userStmt.setString(4, patient.getEmail());
                userStmt.setString(5, patient.getContactNumber());
                userStmt.setString(6, patient.getAddress());
                userStmt.setBoolean(7, patient.isActive());
                userStmt.executeUpdate();
            }
            
            // Insert into patients table
            String patientSql = "INSERT INTO patients (patient_id, username, age, gender, blood_type, emergency_contact, medical_history, allergies, current_medications) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement patientStmt = conn.prepareStatement(patientSql)) {
                patientStmt.setString(1, patient.getPatientId());
                patientStmt.setString(2, patient.getUsername());
                patientStmt.setInt(3, patient.getAge());
                patientStmt.setString(4, patient.getGender());
                patientStmt.setString(5, patient.getBloodType());
                patientStmt.setString(6, patient.getEmergencyContact());
                patientStmt.setString(7, patient.getMedicalHistory());
                
                // Convert lists to comma-separated strings
                String allergiesStr = String.join(",", patient.getAllergies());
                String medicationsStr = String.join(",", patient.getCurrentMedications());
                
                patientStmt.setString(8, allergiesStr);
                patientStmt.setString(9, medicationsStr);
                patientStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("Duplicate entry")) {
                // Already exists, treat as success to avoid noisy logs
                return true;
            }
            System.err.println("Error saving patient: " + msg);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Save a doctor to the database
     * @param doctor doctor to save
     * @return true if successful, false otherwise
     */
    public boolean saveDoctor(Doctor doctor) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert into users table
            String userSql = "INSERT INTO users (username, password, full_name, email, contact_number, address, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                userStmt.setString(1, doctor.getUsername());
                userStmt.setString(2, doctor.getPassword());
                userStmt.setString(3, doctor.getFullName());
                userStmt.setString(4, doctor.getEmail());
                userStmt.setString(5, doctor.getContactNumber());
                userStmt.setString(6, doctor.getAddress());
                userStmt.setBoolean(7, doctor.isActive());
                userStmt.executeUpdate();
            }
            
            // Insert into doctors table
            String doctorSql = "INSERT INTO doctors (doctor_id, username, specialization, license_number, experience_years, qualifications) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement doctorStmt = conn.prepareStatement(doctorSql)) {
                doctorStmt.setString(1, doctor.getDoctorId());
                doctorStmt.setString(2, doctor.getUsername());
                doctorStmt.setString(3, doctor.getSpecialization());
                doctorStmt.setString(4, doctor.getLicenseNumber());
                doctorStmt.setInt(5, doctor.getExperienceYears());
                String qualificationsStr = String.join(",", doctor.getQualifications() != null ? doctor.getQualifications() : java.util.Collections.emptyList());
                doctorStmt.setString(6, qualificationsStr);
                doctorStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("Duplicate entry")) {
                return true;
            }
            System.err.println("Error saving doctor: " + msg);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Save an admin to the database
     * @param admin admin to save
     * @return true if successful, false otherwise
     */
    public boolean saveAdmin(Admin admin) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert into users table
            String userSql = "INSERT INTO users (username, password, full_name, email, contact_number, address, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                userStmt.setString(1, admin.getUsername());
                userStmt.setString(2, admin.getPassword());
                userStmt.setString(3, admin.getFullName());
                userStmt.setString(4, admin.getEmail());
                userStmt.setString(5, admin.getContactNumber());
                userStmt.setString(6, admin.getAddress());
                userStmt.setBoolean(7, admin.isActive());
                userStmt.executeUpdate();
            }
            
            // Insert into admins table
            String adminSql = "INSERT INTO admins (admin_id, username, permissions) VALUES (?, ?, ?)";
            try (PreparedStatement adminStmt = conn.prepareStatement(adminSql)) {
                adminStmt.setString(1, admin.getAdminId());
                adminStmt.setString(2, admin.getUsername());
                String permissionsStr = String.join(",", admin.getPermissions() != null ? admin.getPermissions() : java.util.Collections.emptyList());
                adminStmt.setString(3, permissionsStr);
                adminStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("Duplicate entry")) {
                return true;
            }
            System.err.println("Error saving admin: " + msg);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Get all patients from the database
     * @return list of patients
     */
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT p.*, u.password, u.full_name, u.email, u.contact_number, u.address FROM patients p JOIN users u ON p.username = u.username";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("contact_number"),
                    rs.getString("address"),
                    rs.getString("patient_id"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("blood_type"),
                    rs.getString("emergency_contact")
                );
                
                // Set additional fields
                patient.setMedicalHistory(rs.getString("medical_history"));
                String allergies = rs.getString("allergies");
                if (allergies != null && !allergies.isEmpty()) {
                    for (String allergy : allergies.split(",")) {
                        patient.addAllergy(allergy.trim());
                    }
                }
                
                String medications = rs.getString("current_medications");
                if (medications != null && !medications.isEmpty()) {
                    for (String med : medications.split(",")) {
                        patient.addCurrentMedication(med.trim());
                    }
                }
                
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patients: " + e.getMessage());
        }
        
        return patients;
    }
    
    /**
     * Get all doctors from the database
     * @return list of doctors
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT d.*, u.password, u.full_name, u.email, u.contact_number, u.address FROM doctors d JOIN users u ON d.username = u.username";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("contact_number"),
                    rs.getString("address"),
                    rs.getString("doctor_id"),
                    rs.getString("specialization"),
                    rs.getString("license_number"),
                    rs.getInt("experience_years")
                );
                
                // Set qualifications
                String qualifications = rs.getString("qualifications");
                if (qualifications != null && !qualifications.isEmpty()) {
                    for (String qual : qualifications.split(",")) {
                        doctor.addQualification(qual.trim());
                    }
                }
                
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving doctors: " + e.getMessage());
        }
        
        return doctors;
    }
    
    /**
     * Get all admins from the database
     * @return list of admins
     */
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT a.*, u.password, u.full_name, u.email, u.contact_number, u.address FROM admins a JOIN users u ON a.username = u.username";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Admin admin = new Admin(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("contact_number"),
                    rs.getString("address"),
                    rs.getString("admin_id")
                );
                
                // Set permissions
                String permissions = rs.getString("permissions");
                if (permissions != null && !permissions.isEmpty()) {
                    for (String perm : permissions.split(",")) {
                        admin.addPermission(perm.trim());
                    }
                }
                
                admins.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving admins: " + e.getMessage());
        }
        
        return admins;
    }
    
    /**
     * Save an appointment to the database
     * @param appointment appointment to save
     * @return true if successful, false otherwise
     */
    public boolean saveAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (appointment_id, doctor_id, patient_id, appointment_date, appointment_time, time_slot, reason, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, appointment.getAppointmentId());
            stmt.setString(2, appointment.getDoctorId());
            stmt.setString(3, appointment.getPatientId());
            stmt.setDate(4, Date.valueOf(appointment.getDate()));
            stmt.setTime(5, Time.valueOf(appointment.getTime()));
            stmt.setString(6, appointment.getTimeSlot());
            stmt.setString(7, appointment.getReason());
            stmt.setString(8, appointment.getStatus());
            stmt.setString(9, appointment.getNotes());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("Duplicate entry")) {
                return true;
            }
            System.err.println("Error saving appointment: " + msg);
            return false;
        }
    }
    
    /**
     * Save a medical record to the database
     * @param record medical record to save
     * @return true if successful, false otherwise
     */
    public boolean saveMedicalRecord(MedicalRecord record) {
        String sql = "INSERT INTO medical_records (record_id, patient_id, doctor_id, diagnosis, prescription, treatment, notes, status, symptoms, medications) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, record.getRecordId());
            stmt.setString(2, record.getPatientId());
            stmt.setString(3, record.getDoctorId());
            stmt.setString(4, record.getDiagnosis());
            stmt.setString(5, record.getPrescription());
            stmt.setString(6, record.getTreatment());
            stmt.setString(7, record.getNotes());
            stmt.setString(8, record.getStatus());
            String symptomsStr = record.getSymptoms() != null ? String.join(",", record.getSymptoms()) : "";
            stmt.setString(9, symptomsStr);
            String medicationsStr = record.getMedications() != null ? String.join(",", record.getMedications()) : "";
            stmt.setString(10, medicationsStr);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("Duplicate entry")) {
                return true;
            }
            System.err.println("Error saving medical record: " + msg);
            return false;
        }
    }
    
    /**
     * Save a prescription to the database
     * @param prescription prescription to save
     * @return true if successful, false otherwise
     */
    public boolean savePrescription(Prescription prescription) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert into prescriptions table
            String sql = "INSERT INTO prescriptions (prescription_id, patient_id, doctor_id, instructions, status, valid_until, refills_remaining) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, prescription.getPrescriptionId());
                stmt.setString(2, prescription.getPatientId());
                stmt.setString(3, prescription.getDoctorId());
                stmt.setString(4, prescription.getInstructions());
                stmt.setString(5, prescription.getStatus());
                stmt.setDate(6, prescription.getValidUntil() != null ? Date.valueOf(prescription.getValidUntil()) : null);
                stmt.setInt(7, prescription.getRefillsRemaining());
                stmt.executeUpdate();
            }
            
            // Insert medications
            String medSql = "INSERT INTO prescription_medications (prescription_id, medication_name, dosage, frequency, duration) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement medStmt = conn.prepareStatement(medSql)) {
                for (Prescription.Medication med : prescription.getMedications()) {
                    medStmt.setString(1, prescription.getPrescriptionId());
                    medStmt.setString(2, med.getMedicationName());
                    medStmt.setString(3, med.getDosage());
                    medStmt.setString(4, med.getFrequency());
                    medStmt.setString(5, med.getDuration());
                    medStmt.addBatch();
                }
                medStmt.executeBatch();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("Duplicate entry")) {
                return true;
            }
            System.err.println("Error saving prescription: " + msg);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }
}
