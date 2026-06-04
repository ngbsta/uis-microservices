package com.uis.estudyrecord.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exam_result")
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long courseId;
    private Long sittingId;
    private String grade;
    private int credits;
    private LocalDate date;

    public ExamResult() {
    }

    public ExamResult(Long studentId, Long courseId, Long sittingId, String grade, int credits, LocalDate date) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.sittingId = sittingId;
        this.grade = grade;
        this.credits = credits;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getSittingId() { return sittingId; }
    public void setSittingId(Long sittingId) { this.sittingId = sittingId; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
