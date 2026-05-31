package com.uis.estudyrecord.controller;

import com.uis.estudyrecord.domain.Enrollment;
import com.uis.estudyrecord.domain.ExamResult;
import com.uis.estudyrecord.domain.Notification;
import com.uis.estudyrecord.domain.Student;
import com.uis.estudyrecord.service.StudyRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StudyRecordController {

    private final StudyRecordService service;

    public StudyRecordController(StudyRecordService service) {
        this.service = service;
    }

    // Notifications (Student 1 -- 0..* Notification)
    @GetMapping("/notifications")
    public List<Notification> getNotifications(@RequestParam Long studentId) {
        return service.getNotifications(studentId);
    }

    @PutMapping("/notifications/{id}/read")
    public Notification markRead(@PathVariable Long id) {
        return service.markNotificationRead(id);
    }

    // Search & select a student
    @GetMapping("/students")
    public List<Student> getStudents() {
        return service.getStudents();
    }

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable Long id) {
        return service.getStudent(id);
    }

    // Study overview (basic + whole study): enrolments, exam history, credits
    @GetMapping("/students/{id}/overview")
    public Map<String, Object> getOverview(@PathVariable Long id) {
        return service.getStudyOverview(id);
    }

    // View past exam sittings (exam history)
    @GetMapping("/students/{id}/exam-history")
    public List<ExamResult> getExamHistory(@PathVariable Long id) {
        return service.getResults(id);
    }

    // Track credits obtained
    @GetMapping("/students/{id}/credits")
    public Map<String, Object> getCredits(@PathVariable Long id) {
        return Map.of("studentId", id, "totalCredits", service.getTotalCredits(id));
    }

    @GetMapping("/enrollments")
    public List<Enrollment> getEnrollments(@RequestParam(required = false) Long studentId) {
        return service.getEnrollments(studentId);
    }

    // Called by exam-registration-service (inter-service communication)
    @GetMapping("/enrollments/exists")
    public Map<String, Boolean> isEnrolled(@RequestParam Long studentId, @RequestParam Long courseId) {
        return Map.of("enrolled", service.isEnrolled(studentId, courseId));
    }

    @PostMapping("/enrollments")
    public ResponseEntity<Enrollment> addEnrollment(@RequestBody Enrollment enrollment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addEnrollment(enrollment));
    }

    @GetMapping("/results")
    public List<ExamResult> getResults(@RequestParam Long studentId) {
        return service.getResults(studentId);
    }

    // Teacher: enter exam result
    @PostMapping("/results")
    public ResponseEntity<ExamResult> addResult(@RequestBody ExamResult result) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addResult(result));
    }

    // Teacher: enter / update exam result (grade, credits awarded)
    @PutMapping("/results/{id}")
    public ExamResult updateResult(@PathVariable Long id,
                                   @RequestParam String grade,
                                   @RequestParam int attempt,
                                   @RequestParam int credits) {
        return service.updateResult(id, grade, attempt, credits);
    }
}
