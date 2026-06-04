package com.uis.examregistration.dto;

/**
 * Response DTO for the inter-service enrolment check against e-study-record-service.
 * The RestTemplate call deserializes the JSON response into this DTO.
 */
public class EnrollmentStatusDTO {
    private Long studentId;
    private Long courseId;
    private boolean enrolled;

    public EnrollmentStatusDTO() {
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public boolean isEnrolled() { return enrolled; }
    public void setEnrolled(boolean enrolled) { this.enrolled = enrolled; }
}
