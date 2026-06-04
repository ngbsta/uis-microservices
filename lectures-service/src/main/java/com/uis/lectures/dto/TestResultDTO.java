package com.uis.lectures.dto;

import java.time.LocalDateTime;

/**
 * A mid-term test result enriched with the real course name (owned by this service)
 * and the real student name (fetched from e-study-record). The UI shows names, never ids.
 */
public class TestResultDTO {
    private Long id;
    private Long courseId;
    private String courseName;
    private Long studentId;
    private String studentName;
    private double score;
    private int activityPoint;
    private boolean released;
    private LocalDateTime submittedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public int getActivityPoint() { return activityPoint; }
    public void setActivityPoint(int activityPoint) { this.activityPoint = activityPoint; }
    public boolean isReleased() { return released; }
    public void setReleased(boolean released) { this.released = released; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
