package com.uis.examregistration.dto;

import jakarta.validation.constraints.NotNull;

/** Incoming request body for registering to an exam sitting. */
public class RegistrationRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long sittingId;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getSittingId() { return sittingId; }
    public void setSittingId(Long sittingId) { this.sittingId = sittingId; }
}
