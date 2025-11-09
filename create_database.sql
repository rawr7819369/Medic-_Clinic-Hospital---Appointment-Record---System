-- Create database
CREATE DATABASE IF NOT EXISTS mediconnect_db;
USE mediconnect_db;

-- Users table (base table)
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admins table
CREATE TABLE IF NOT EXISTS admins (
    admin_id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    permissions TEXT,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Doctors table
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    experience_years INT NOT NULL,
    qualifications TEXT,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Patients table
CREATE TABLE IF NOT EXISTS patients (
    patient_id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) NOT NULL,
    blood_type VARCHAR(10) NOT NULL,
    emergency_contact VARCHAR(50) NOT NULL,
    medical_history TEXT,
    allergies TEXT,
    current_medications TEXT,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id VARCHAR(20) PRIMARY KEY,
    doctor_id VARCHAR(20) NOT NULL,
    patient_id VARCHAR(20) NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    time_slot VARCHAR(20) NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    notes TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- Medical records table
CREATE TABLE IF NOT EXISTS medical_records (
    record_id VARCHAR(20) PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    doctor_id VARCHAR(20) NOT NULL,
    diagnosis TEXT NOT NULL,
    prescription TEXT,
    treatment TEXT,
    notes TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    symptoms TEXT,
    medications TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE
);

-- Prescriptions table
CREATE TABLE IF NOT EXISTS prescriptions (
    prescription_id VARCHAR(20) PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    doctor_id VARCHAR(20) NOT NULL,
    instructions TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    valid_until DATE,
    refills_remaining INT DEFAULT 0,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE
);

-- Prescription medications table
CREATE TABLE IF NOT EXISTS prescription_medications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prescription_id VARCHAR(20) NOT NULL,
    medication_name VARCHAR(100) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    duration VARCHAR(50) NOT NULL,
    FOREIGN KEY (prescription_id) REFERENCES prescriptions(prescription_id) ON DELETE CASCADE
);

-- Insert default admin user
INSERT INTO users (username, password, full_name, email, contact_number, address, is_active) 
VALUES ('admin', 'Admin123!', 'System Administrator', 'admin@mediconnect.com', '1234567890', 'System Address', TRUE);

INSERT INTO admins (admin_id, username, permissions) 
VALUES ('ADM001', 'admin', 'ALL');
