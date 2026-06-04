package com.uis.lectures.dto;

/**
 * A student as owned by e-study-record-service. Fetched via the inter-service call so
 * the lectures UI lists real students from the single source of truth (e-study-record),
 * instead of hardcoding them in the frontend.
 */
public class StudentDTO {
    private Long id;
    private String name;

    public StudentDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
