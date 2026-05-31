package com.uis.lectures.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private int credits;
    private String language;
    private boolean compulsory;
    private Long programmeId;
    private Long teacherId;

    public Course() {
    }

    public Course(String code, String name, int credits, String language, boolean compulsory,
                  Long programmeId, Long teacherId) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.language = language;
        this.compulsory = compulsory;
        this.programmeId = programmeId;
        this.teacherId = teacherId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public boolean isCompulsory() { return compulsory; }
    public void setCompulsory(boolean compulsory) { this.compulsory = compulsory; }
    public Long getProgrammeId() { return programmeId; }
    public void setProgrammeId(Long programmeId) { this.programmeId = programmeId; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}
