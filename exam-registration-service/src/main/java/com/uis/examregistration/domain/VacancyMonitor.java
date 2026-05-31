package com.uis.examregistration.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** UC-07: a student watches a full sitting to be alerted when a place frees up. */
@Entity
@Table(name = "vacancy_monitor")
public class VacancyMonitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long sittingId;
    private LocalDateTime createdAt;
    private boolean notified;

    public VacancyMonitor() {
    }

    public VacancyMonitor(Long studentId, Long sittingId) {
        this.studentId = studentId;
        this.sittingId = sittingId;
        this.createdAt = LocalDateTime.now();
        this.notified = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getSittingId() { return sittingId; }
    public void setSittingId(Long sittingId) { this.sittingId = sittingId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isNotified() { return notified; }
    public void setNotified(boolean notified) { this.notified = notified; }
}
