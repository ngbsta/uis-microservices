package com.uis.estudyrecord.dto;

/**
 * Returned by GET /api/enrollments/exists. The inter-service call from
 * exam-registration-service deserializes the response into this DTO
 * (so the communication "ends with a DTO" on both sides).
 */
public class EnrollmentStatusDTO {
    private Long studentId;
    private Long courseId;
    private boolean enrolled;

    public EnrollmentStatusDTO() {
    }

    public EnrollmentStatusDTO(Long studentId, Long courseId, boolean enrolled) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolled = enrolled;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public boolean isEnrolled() { return enrolled; }
    public void setEnrolled(boolean enrolled) { this.enrolled = enrolled; }
}
