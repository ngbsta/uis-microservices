package com.uis.estudyrecord.dto;

import java.time.LocalDate;

/**
 * Exam result enriched with the course name resolved from lectures-service.
 * The results endpoints "end with a DTO".
 */
public class ExamResultDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String courseName;
    private Long sittingId;
    private String grade;
    private int credits;
    private LocalDate date;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Long getSittingId() { return sittingId; }
    public void setSittingId(Long sittingId) { this.sittingId = sittingId; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
