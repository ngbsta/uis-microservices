package com.uis.examregistration.dto;

import java.time.LocalDateTime;

public class SittingDTO {
    private Long id;
    private Long courseId;
    private String courseName;   // resolved from lectures-service (single source of truth)
    private Long teacherId;
    private LocalDateTime date;
    private String room;
    private String type;
    private int capacity;
    private int registeredCount;
    private LocalDateTime registerFrom;
    private LocalDateTime registerUntil;
    private LocalDateTime unregisterUntil;
    private boolean registrationOpen;
    private boolean freeCapacity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
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
    public LocalDateTime getRegisterFrom() { return registerFrom; }
    public void setRegisterFrom(LocalDateTime registerFrom) { this.registerFrom = registerFrom; }
    public LocalDateTime getRegisterUntil() { return registerUntil; }
    public void setRegisterUntil(LocalDateTime registerUntil) { this.registerUntil = registerUntil; }
    public LocalDateTime getUnregisterUntil() { return unregisterUntil; }
    public void setUnregisterUntil(LocalDateTime unregisterUntil) { this.unregisterUntil = unregisterUntil; }
    public boolean isRegistrationOpen() { return registrationOpen; }
    public void setRegistrationOpen(boolean registrationOpen) { this.registrationOpen = registrationOpen; }
    public boolean isFreeCapacity() { return freeCapacity; }
    public void setFreeCapacity(boolean freeCapacity) { this.freeCapacity = freeCapacity; }
}
