package com.uis.estudyrecord.controller;

import com.uis.estudyrecord.domain.Enrollment;
import com.uis.estudyrecord.domain.Student;
import com.uis.estudyrecord.dto.CourseDTO;
import com.uis.estudyrecord.dto.EnrollmentStatusDTO;
import com.uis.estudyrecord.dto.ExamResultDTO;
import com.uis.estudyrecord.dto.ResultRequest;
import com.uis.estudyrecord.service.StudyRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API of the E-Study Record service (port 8082): students, enrolments and exam
 * results/grades. Course names and credits, and the mid-term used to compute the grade,
 * are fetched from lectures-service (8083). Every endpoint returns a DTO, not a JPA entity.
 */
@RestController
@RequestMapping("/api")
public class StudyRecordController {

    private final StudyRecordService service;

    public StudyRecordController(StudyRecordService service) {
        this.service = service;
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

    // Course catalogue, fetched from lectures-service (inter-service communication)
    @GetMapping("/courses")
    public List<CourseDTO> getCourses() {
        return service.getCourses();
    }

    // View past exam sittings (exam history) — enriched with course names
    @GetMapping("/students/{id}/exam-history")
    public List<ExamResultDTO> getExamHistory(@PathVariable Long id) {
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

    // Called by exam-registration-service before a registration (inter-service). Returns EnrollmentStatusDTO.
    @GetMapping("/enrollments/exists")
    public EnrollmentStatusDTO isEnrolled(@RequestParam Long studentId, @RequestParam Long courseId) {
        return new EnrollmentStatusDTO(studentId, courseId, service.isEnrolled(studentId, courseId));
    }

    @PostMapping("/enrollments")
    public ResponseEntity<Enrollment> addEnrollment(@RequestBody Enrollment enrollment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addEnrollment(enrollment));
    }

    @GetMapping("/results")
    public List<ExamResultDTO> getResults(@RequestParam Long studentId) {
        return service.getResults(studentId);
    }

    // Teacher: enter exam result (final score only; mid-term pulled from 8083, grade computed). Returns the saved result as a DTO.
    @PostMapping("/results")
    public ResponseEntity<ExamResultDTO> addResult(@RequestBody ResultRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addResult(request));
    }

    // Teacher: update the FINAL exam score. Mid-term re-fetched from lectures, grade recomputed. Returns the updated result as a DTO.
    @PutMapping("/results/{id}")
    public ExamResultDTO updateResult(@PathVariable Long id,
                                      @RequestParam double finalScore) {
        return service.updateResult(id, finalScore);
    }
}
