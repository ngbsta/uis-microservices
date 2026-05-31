package com.uis.examregistration.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_registration")
public class ExamRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    private RegStatus status;

    // Student is owned by e-study-record-service -> referenced by id
    private Long studentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_sitting_id")
    private ExamSitting examSitting;

    public ExamRegistration() {
    }

    public ExamRegistration(Long studentId, ExamSitting examSitting, RegStatus status) {
        this.studentId = studentId;
        this.examSitting = examSitting;
        this.status = status;
        this.registeredAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = RegStatus.CANCELLED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
    public RegStatus getStatus() { return status; }
    public void setStatus(RegStatus status) { this.status = status; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public ExamSitting getExamSitting() { return examSitting; }
    public void setExamSitting(ExamSitting examSitting) { this.examSitting = examSitting; }
}
