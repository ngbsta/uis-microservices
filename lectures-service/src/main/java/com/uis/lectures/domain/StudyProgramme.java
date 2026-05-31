package com.uis.lectures.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "study_programme")
public class StudyProgramme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String faculty;

    public StudyProgramme() {
    }

    public StudyProgramme(String code, String name, String faculty) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
}
