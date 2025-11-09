
---

## 2. Appointment Approval Process
- [x] **Add "Pending Approval" Status to New Appointments**
  - When an appointment is booked, set its status to "Pending Approval."

- [x] **Create Approval Button on Doctor’s Dashboard**
  - Allow doctors to approve or deny appointments from their dashboard.
  
- [x] **Update Appointment Status on Approval/Denial**
  - Change appointment status to "Confirmed" when approved.
  - Change status to "Denied" when canceled.
  
- [x] **Add Reason for Denial**
  - Allow doctors to provide a reason when denying an appointment.



---

## 3. Medical Records Management
- [x] **Add "Medical Records" Section to Doctor’s Dashboard**
  - Implement UI section where doctors can add prescriptions and notes.
  
- [x] **Create Prescription Form**
  - Implement a form/modal for doctors to input prescription details (e.g., medication name, dosage, instructions).

- [x] **Save and Store Medical Records**
  - Ensure that prescriptions and medical notes are saved in the database, linked to the correct patient.



---

## 4. Doctors Initiating Appointments
- [x] **Create Appointment Creation Form for Doctors**
  - Provide a UI for doctors to initiate appointments for their patients.
  - Include fields for selecting the patient, date/time, and reason for the appointment.

- [x] **Save New Appointments**
  - Save initiated appointments to the database with "Pending Approval" status.



---

## 5. Doctor Appointment Cancellation with Reason
- [x] **Create Cancellation Feature on Appointment Details Page**
  - Allow doctors to cancel appointments with an option to select or type a reason for cancellation.

- [x] **Update Appointment Status to "Cancelled"**
  - When cancelled, update the appointment status to "Cancelled."

- [x] **Track Reason for Cancellation**
  - Allow doctors to select or input a reason for cancellation.


---

## 6. Database Design and Backend Integration
- [x] **Create Appointment Table**
  - Fields: `appointment_id`, `patient_id`, `doctor_id`, `status`, `scheduled_time`, `cancellation_reason`, etc.

- [x] **Create Prescription Table**
  - Fields: `prescription_id`, `doctor_id`, `patient_id`, `medication_name`, `dosage`, `instructions`, etc.

- [x] **Implement Enum for Appointment Status**
  - Pending Approval
  - Confirmed
  - Cancelled

- [x] **Ensure Relationship Between Doctors, Patients, and Appointments**
  - Make sure appointments are tied to both patients and doctors.

---

## 7. User Interface (UI) and User Experience
- [x] **Design Doctor’s Dashboard**
  - Display appointment list with filter options (Pending, Confirmed, Cancelled).
  - Display option to approve/deny appointments.
  - Add the "Medical Records" section for prescriptions.

- [x] **Create Appointment Creation Form**
  - Allow doctors to create appointments for their patients.

- [x] **Create Appointment Cancellation Interface**
  - Allow doctors to cancel appointments and input a reason.

- [x] **Design Medical Records Section**
  - Allow doctors to add prescriptions and view history.


---

# Patient Functionality Todo List

This is the todo list for implementing patient-specific functionalities in the windsurf application.

## 1. Photo Uploader for Scans (Patient Side)
- [x] **Create Photo Upload Form for Patients**
  - Allow patients to upload image files (e.g., JPG, PNG, PDF) from their devices.

- [x] **Validate File Uploads**
  - Ensure that only valid file types (images or PDFs) are accepted.
  - Enforce file size limits (e.g., max 10MB per file).

- [x] **Implement Image Preview Feature**
  - Show a preview of the uploaded scan before the patient submits it.

- [x] **Save Uploaded Scans to Database**
  - Store the uploaded images in a secure location and associate them with the patient's record and appointment.

- [x] **Create Doctor’s Access to Uploaded Scans**
  - Allow doctors to view the patient’s scans in the medical history section.

---

## 2. Medical History Page (Patient Side)
- [x] **Create Medical History Dashboard**
  - Design a dashboard that displays a timeline or list of the patient’s past appointments and associated records.

- [x] **Display Uploaded Scans**
  - Show thumbnails of uploaded scans (e.g., X-rays, MRIs) with an option to view or download the full image.

- [x] **Display Prescriptions and Doctor Notes**
  - Include a list of previous prescriptions, medical records, and doctor's notes tied to the patient's past appointments.

- [x] **Implement Pagination or Infinite Scroll**
  - Ensure that the medical history page is easily navigable when there are many records, using pagination or infinite scroll.

---

## 3. Profile Page (Patient Side)
- [x] **Create Profile View Page**
  - Display the patient’s personal information (name, email, phone number, etc.).

- [x] **Allow Profile Editing**
  - Allow patients to update their contact information (e.g., phone number, email).

- [x] **Allow Password Update**
  - Provide an option for patients to change their password.

- [ ] **Profile Picture (Optional)**
  - Optionally allow the patient to upload or update their profile picture.

- [x] **Save Changes to Profile**
  - Ensure all profile changes are saved to the database and reflected in the patient’s account.

---

---

## 5. Backend and Database Integration
- [x] **Create Scans Table in Database**
  - Store the scan data with fields such as `scan_id`, `patient_id`, `appointment_id`, `file_path`, `file_type`, etc.

- [x] **Link Scans to Patient and Appointment**
  - Ensure that each uploaded scan is properly linked to the correct patient and appointment record.

- [x] **Create Prescriptions Table**
  - Ensure that prescriptions are stored with the appropriate patient and appointment information.

---

## 6. Testing
- [ ] **Unit Tests for File Upload**
  - Test the file upload feature to ensure it accepts only valid files, handles file size limitations, and stores scans securely.

- [ ] **Unit Tests for Medical History Page**
  - Verify that the patient’s medical history (scans, prescriptions, doctor notes) displays correctly on the page.

- [ ] **Unit Tests for Profile Update**
  - Test that profile editing and
