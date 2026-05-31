package com.uis.lectures.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "assessment")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    private String title;
    private double weight;
    private int maxScore;
    private String type;

    public Assessment() {
    }

    public Assessment(Long courseId, String title, double weight, int maxScore, String type) {
        this.courseId = courseId;
        this.title = title;
        this.weight = weight;
        this.maxScore = maxScore;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
