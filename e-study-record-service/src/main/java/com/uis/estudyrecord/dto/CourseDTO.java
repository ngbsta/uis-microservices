package com.uis.estudyrecord.dto;

/**
 * A course as owned by lectures-service. Fetched via the inter-service call so
 * e-study-record can show real class names next to a student's grades.
 */
public class CourseDTO {
    private Long id;
    private String code;
    private String name;
    private int credits;

    public CourseDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
}
