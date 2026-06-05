package com.uis.estudyrecord.dto;

import java.time.LocalDate;

/**
 * Request body for creating an exam result. The teacher provides only the FINAL score
 * (+ which course/student/sitting); the mid-term, overall and grade are computed server-side.
 * Wrapper types so omitted fields stay null instead of failing primitive binding.
 */
public class ResultRequest {
    private Long studentId;
    private Long courseId;
    private Long sittingId;
    private Double finalScore;
    private LocalDate date;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getSittingId() { return sittingId; }
    public void setSittingId(Long sittingId) { this.sittingId = sittingId; }
    public Double getFinalScore() { return finalScore; }
    public void setFinalScore(Double finalScore) { this.finalScore = finalScore; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
