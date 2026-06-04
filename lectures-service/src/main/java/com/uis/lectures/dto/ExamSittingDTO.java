package com.uis.lectures.dto;

import java.time.LocalDateTime;

/**
 * DTO for an exam sitting fetched from exam-registration-service.
 * The inter-service RestTemplate call deserializes into this DTO, and the
 * lectures REST endpoint returns it (the call "ends with a DTO").
 */
public class ExamSittingDTO {
    private Long id;
    private Long courseId;
    private Long teacherId;
    private LocalDateTime date;
    private String room;
    private String type;
    private int capacity;
    private int registeredCount;
    private boolean registrationOpen;
    private boolean freeCapacity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getRegisteredCount() { return registeredCount; }
    public void setRegisteredCount(int registeredCount) { this.registeredCount = registeredCount; }
    public boolean isRegistrationOpen() { return registrationOpen; }
    public void setRegistrationOpen(boolean registrationOpen) { this.registrationOpen = registrationOpen; }
    public boolean isFreeCapacity() { return freeCapacity; }
    public void setFreeCapacity(boolean freeCapacity) { this.freeCapacity = freeCapacity; }
}
