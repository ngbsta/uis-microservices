package com.uis.lectures.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Umer's "My Lectures Sheet": a course test result, released after submission. */
@Entity
@Table(name = "test_result")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    private Long studentId;
    private double score;
    private boolean released;
    private LocalDateTime submittedAt;

    public TestResult() {
    }

    public TestResult(Long courseId, Long studentId, double score, boolean released, LocalDateTime submittedAt) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.score = score;
        this.released = released;
        this.submittedAt = submittedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public boolean isReleased() { return released; }
    public void setReleased(boolean released) { this.released = released; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
