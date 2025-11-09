-- Update database schema to fix column size issues
USE mediconnect_db;

-- Update emergency_contact column size
ALTER TABLE patients MODIFY COLUMN emergency_contact VARCHAR(50) NOT NULL;

-- Update other potentially problematic columns
ALTER TABLE users MODIFY COLUMN contact_number VARCHAR(30) NOT NULL;
ALTER TABLE users MODIFY COLUMN email VARCHAR(150) UNIQUE NOT NULL;
ALTER TABLE users MODIFY COLUMN full_name VARCHAR(150) NOT NULL;

-- Update doctors table
ALTER TABLE doctors MODIFY COLUMN specialization VARCHAR(150) NOT NULL;
ALTER TABLE doctors MODIFY COLUMN license_number VARCHAR(100) UNIQUE NOT NULL;

-- Update appointments table
ALTER TABLE appointments MODIFY COLUMN time_slot VARCHAR(30) NOT NULL;
ALTER TABLE appointments MODIFY COLUMN status VARCHAR(30) DEFAULT 'PENDING';
-- Add reason columns if not present
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS cancellation_reason TEXT NULL;
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS denial_reason TEXT NULL;

-- Update medical_records table
ALTER TABLE medical_records MODIFY COLUMN status VARCHAR(30) DEFAULT 'ACTIVE';

-- Update prescriptions table
ALTER TABLE prescriptions MODIFY COLUMN status VARCHAR(30) DEFAULT 'ACTIVE';

-- Update prescription_medications table
ALTER TABLE prescription_medications MODIFY COLUMN medication_name VARCHAR(150) NOT NULL;
ALTER TABLE prescription_medications MODIFY COLUMN dosage VARCHAR(100) NOT NULL;
ALTER TABLE prescription_medications MODIFY COLUMN frequency VARCHAR(100) NOT NULL;
ALTER TABLE prescription_medications MODIFY COLUMN duration VARCHAR(100) NOT NULL;

-- Create scans table (if not exists)
CREATE TABLE IF NOT EXISTS scans (
  scan_id VARCHAR(50) PRIMARY KEY,
  patient_id VARCHAR(50) NOT NULL,
  appointment_id VARCHAR(50) NULL,
  file_path VARCHAR(500) NOT NULL,
  file_type VARCHAR(50) NOT NULL,
  file_size BIGINT NOT NULL,
  uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  description VARCHAR(255) NULL,
  CONSTRAINT fk_scans_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- Show updated schema
DESCRIBE patients;
DESCRIBE users;
DESCRIBE doctors;
