package com.uis.lectures.domain;

import jakarta.persistence.*;

/** Umer's "My Lectures Sheet": weekly schedule + attendance for a course. */
@Entity
@Table(name = "timetable")
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;

    @Column(length = 2000)
    private String weeklySlots;     // e.g. "Mon 09:00-11:00 Q01; Wed 13:00-15:00 Q02"
    @Column(length = 2000)
    private String attendanceLog;   // e.g. "2026-03-02: present; 2026-03-09: absent"

    public Timetable() {
    }

    public Timetable(Long courseId, String weeklySlots, String attendanceLog) {
        this.courseId = courseId;
        this.weeklySlots = weeklySlots;
        this.attendanceLog = attendanceLog;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getWeeklySlots() { return weeklySlots; }
    public void setWeeklySlots(String weeklySlots) { this.weeklySlots = weeklySlots; }
    public String getAttendanceLog() { return attendanceLog; }
    public void setAttendanceLog(String attendanceLog) { this.attendanceLog = attendanceLog; }
}
