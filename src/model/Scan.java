package model;

import java.time.LocalDateTime;

public class Scan {
    private String scanId;
    private String patientId;
    private String appointmentId;
    private String filePath;
    private String fileType;
    private long fileSizeBytes;
    private LocalDateTime uploadedAt;
    private String description;

    public Scan(String scanId, String patientId, String appointmentId, String filePath, String fileType, long fileSizeBytes) {
        this.scanId = scanId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSizeBytes = fileSizeBytes;
        this.uploadedAt = LocalDateTime.now();
        this.description = "";
    }

    public String getScanId() { return scanId; }
    public void setScanId(String scanId) { this.scanId = scanId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
