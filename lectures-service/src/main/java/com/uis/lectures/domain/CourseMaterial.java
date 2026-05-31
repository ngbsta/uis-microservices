package com.uis.lectures.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_material")
public class CourseMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    private String title;
    private String fileUrl;
    private LocalDateTime uploadedAt;

    public CourseMaterial() {
    }

    public CourseMaterial(Long courseId, String title, String fileUrl, LocalDateTime uploadedAt) {
        this.courseId = courseId;
        this.title = title;
        this.fileUrl = fileUrl;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
