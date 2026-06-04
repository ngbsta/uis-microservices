package com.uis.lectures.domain;

import jakarta.persistence.*;

/**
 * One weekly attendance record for a student in a course — exactly what the real
 * Mendel UIS "My Lectures Sheet" shows (weeks 1..N marked present/absent/excused).
 * This belongs to the lectures module; the final exam grade lives in e-study-record.
 */
@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long courseId;
    private int week;          // teaching week number (1..N)
    private String status;     // "present" | "absent" | "excused"

    public Attendance() {
    }

    public Attendance(Long studentId, Long courseId, int week, String status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.week = week;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public int getWeek() { return week; }
    public void setWeek(int week) { this.week = week; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
