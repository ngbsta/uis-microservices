package com.uis.estudyrecord.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long courseId;
    private Long studyPeriodId;
    private String status;

    public Enrollment() {
    }

    public Enrollment(Long studentId, Long courseId, Long studyPeriodId, String status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.studyPeriodId = studyPeriodId;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getStudyPeriodId() { return studyPeriodId; }
    public void setStudyPeriodId(Long studyPeriodId) { this.studyPeriodId = studyPeriodId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
