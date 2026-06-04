package com.uis.examregistration.dto;

/**
 * A course as owned by lectures-service. Fetched via the inter-service call so the
 * exam-registration responses can carry the real course NAME (single source of truth
 * = lectures-service), instead of the frontend hardcoding names.
 */
public class CourseDTO {
    private Long id;
    private String code;
    private String name;

    public CourseDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
