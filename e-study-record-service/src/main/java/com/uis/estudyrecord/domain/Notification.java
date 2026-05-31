package com.uis.estudyrecord.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Notification class from the conceptual / implementation class diagram
 * (Student 1 -- 0..* Notification). Carries markAsRead() as in the class diagram.
 */
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;

    public Notification() {
    }

    public Notification(Long studentId, String message) {
        this.studentId = studentId;
        this.message = message;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    /** Operation from the implementation class diagram. */
    public void markAsRead() {
        this.read = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
