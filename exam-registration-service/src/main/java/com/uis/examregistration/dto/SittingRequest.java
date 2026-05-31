package com.uis.examregistration.dto;

import java.time.LocalDateTime;

/** Teacher creates a new exam sitting. Date/registration windows are optional;
 *  when omitted the sitting opens immediately (handy for the demo). */
public class SittingRequest {
    private Long courseId;
    private Long teacherId;
    private String room;
    private String type;
    private int capacity;
    private LocalDateTime date;
    private LocalDateTime registerFrom;
    private LocalDateTime registerUntil;
    private LocalDateTime unregisterUntil;

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public LocalDateTime getRegisterFrom() { return registerFrom; }
    public void setRegisterFrom(LocalDateTime registerFrom) { this.registerFrom = registerFrom; }
    public LocalDateTime getRegisterUntil() { return registerUntil; }
    public void setRegisterUntil(LocalDateTime registerUntil) { this.registerUntil = registerUntil; }
    public LocalDateTime getUnregisterUntil() { return unregisterUntil; }
    public void setUnregisterUntil(LocalDateTime unregisterUntil) { this.unregisterUntil = unregisterUntil; }
}
