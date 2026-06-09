package com.uis.examregistration.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_sitting")
public class ExamSitting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private String room;
    private String type;
    private int capacity;
    private int registeredCount;
    private LocalDateTime registerFrom;
    private LocalDateTime registerUntil;
    private LocalDateTime unregisterUntil;

    // References to data owned by other services (lectures-service)
    private Long courseId;
    private Long teacherId;

    public ExamSitting() {
    }

    public ExamSitting(LocalDateTime date, String room, String type, int capacity,
                       LocalDateTime registerFrom, LocalDateTime registerUntil, LocalDateTime unregisterUntil,
                       Long courseId, Long teacherId) {
        this.date = date;
        this.room = room;
        this.type = type;
        this.capacity = capacity;
        this.registeredCount = 0;
        this.registerFrom = registerFrom;
        this.registerUntil = registerUntil;
        this.unregisterUntil = unregisterUntil;
        this.courseId = courseId;
        this.teacherId = teacherId;
    }

    // --- business logic (mirrors the class & sequence diagrams; used by register()) ---

    /** Still room left? true while registered count is below the capacity limit. */
    public boolean hasFreeCapacity() {
        return registeredCount < capacity;
    }

    /** Is now within the registration window [registerFrom, registerUntil]? */
    public boolean isRegistrationOpen() {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(registerFrom) && !now.isAfter(registerUntil);
    }

    /** Can a student still cancel? true until the unregister deadline passes. */
    public boolean isUnregisterOpen() {
        return !LocalDateTime.now().isAfter(unregisterUntil);
    }

    /** +1 seat taken — called after a successful registration. */
    public void incrementCount() {
        this.registeredCount++;
    }

    /** -1 seat — called on unregister, freeing a place (never goes below 0). */
    public void decrementCount() {
        if (this.registeredCount > 0) {
            this.registeredCount--;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}
