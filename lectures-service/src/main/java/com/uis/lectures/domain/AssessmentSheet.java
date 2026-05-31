package com.uis.lectures.domain;

import jakarta.persistence.*;

/** Umer's "My Lectures Sheet": per-student assessment log for a course. */
@Entity
@Table(name = "assessment_sheet")
public class AssessmentSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    private Long studentId;
    private double seminarScore;
    private double activityScore;
    private double paperScore;
    private String overallGrade;

    public AssessmentSheet() {
    }

    public AssessmentSheet(Long courseId, Long studentId, double seminarScore, double activityScore,
                           double paperScore, String overallGrade) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.seminarScore = seminarScore;
        this.activityScore = activityScore;
        this.paperScore = paperScore;
        this.overallGrade = overallGrade;
    }

    /** "Define automatic assessment": system-generated overall grade from the scores. */
    public void computeOverallGrade() {
        double total = seminarScore + activityScore + paperScore;
        if (total >= 90) overallGrade = "A";
        else if (total >= 75) overallGrade = "B";
        else if (total >= 60) overallGrade = "C";
        else if (total >= 50) overallGrade = "D";
        else overallGrade = "F";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public double getSeminarScore() { return seminarScore; }
    public void setSeminarScore(double seminarScore) { this.seminarScore = seminarScore; }
    public double getActivityScore() { return activityScore; }
    public void setActivityScore(double activityScore) { this.activityScore = activityScore; }
    public double getPaperScore() { return paperScore; }
    public void setPaperScore(double paperScore) { this.paperScore = paperScore; }
    public String getOverallGrade() { return overallGrade; }
    public void setOverallGrade(String overallGrade) { this.overallGrade = overallGrade; }
}
