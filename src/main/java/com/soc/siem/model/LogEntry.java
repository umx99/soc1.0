package com.soc.siem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sourceIp;
    private String message;
    private LocalDateTime timestamp;
    private String severity; // e.g., INFO, WARN, CRITICAL

    // Default Constructor
    public LogEntry() {}

    public LogEntry(String sourceIp, String message, String severity) {
        this.sourceIp = sourceIp;
        this.message = message;
        this.severity = severity;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters (Important for Spring to read the data)
    public Long getId() { return id; }
    public String getSourceIp() { return sourceIp; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSeverity() { return severity; }
}