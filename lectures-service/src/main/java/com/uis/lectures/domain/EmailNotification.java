package com.uis.lectures.domain;

import jakarta.persistence.*;

/**
 * Per-student, per-course e-mail notification setting — the "set send notifications"
 * toggle shown at the right of each course row in the real Mendel UIS lectures sheet.
 * Clicking it turns sending e-mails on/off for that course.
 */
@Entity
@Table(name = "email_notification")
public class EmailNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long courseId;
    private boolean enabled;

    public EmailNotification() {
    }

    public EmailNotification(Long studentId, Long courseId, boolean enabled) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enabled = enabled;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
