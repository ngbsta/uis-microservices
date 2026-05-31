package com.uis.lectures.domain;

import jakarta.persistence.*;

/** Umer's "My Lectures Sheet": document-server folder for a course. */
@Entity
@Table(name = "course_folder")
public class CourseFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    private String serverPath;
    @Column(length = 2000)
    private String accessPermissions;   // e.g. "teacher:rw; student:r"

    public CourseFolder() {
    }

    public CourseFolder(Long courseId, String serverPath, String accessPermissions) {
        this.courseId = courseId;
        this.serverPath = serverPath;
        this.accessPermissions = accessPermissions;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getServerPath() { return serverPath; }
    public void setServerPath(String serverPath) { this.serverPath = serverPath; }
    public String getAccessPermissions() { return accessPermissions; }
    public void setAccessPermissions(String accessPermissions) { this.accessPermissions = accessPermissions; }
}
