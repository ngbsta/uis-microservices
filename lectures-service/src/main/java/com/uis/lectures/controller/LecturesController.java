package com.uis.lectures.controller;

import com.uis.lectures.domain.*;
import com.uis.lectures.service.LecturesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LecturesController {

    private final LecturesService service;

    public LecturesController(LecturesService service) {
        this.service = service;
    }

    // ---- Courses / lectures / teachers ----
    @GetMapping("/courses")
    public List<Course> getCourses() { return service.getCourses(); }

    @GetMapping("/courses/{id}")
    public Course getCourse(@PathVariable Long id) { return service.getCourse(id); }

    @GetMapping("/courses/{id}/lectures")
    public List<Lecture> getLectures(@PathVariable Long id) { return service.getLectures(id); }

    @GetMapping("/teachers")
    public List<Teacher> getTeachers() { return service.getTeachers(); }

    // Admin: reassign course teacher
    @PutMapping("/courses/{id}/teacher")
    public Course reassignTeacher(@PathVariable Long id, @RequestParam Long teacherId) {
        return service.reassignTeacher(id, teacherId);
    }

    // ---- Student: view course timetable (weekly schedule + attendance) ----
    @GetMapping("/courses/{id}/timetable")
    public Timetable getTimetable(@PathVariable Long id) { return service.getTimetable(id); }

    // ---- Course folder + documents ----
    @GetMapping("/courses/{id}/folder")
    public CourseFolder getFolder(@PathVariable Long id) { return service.getFolder(id); }

    @GetMapping("/courses/{id}/materials")
    public List<CourseMaterial> getMaterials(@PathVariable Long id) { return service.getMaterials(id); }

    @PostMapping("/materials")
    public ResponseEntity<CourseMaterial> uploadMaterial(@RequestBody CourseMaterial material) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.uploadMaterial(material));
    }

    // ---- Assessment sheets ----
    @GetMapping("/assessment-sheets")
    public List<AssessmentSheet> getAssessmentSheets(@RequestParam Long studentId) {
        return service.getAssessmentSheetsByStudent(studentId);
    }

    @PostMapping("/assessment-sheets")
    public ResponseEntity<AssessmentSheet> createSheet(@RequestBody AssessmentSheet sheet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveAssessmentSheet(sheet));
    }

    @PutMapping("/assessment-sheets/{id}/scores")
    public AssessmentSheet updateScores(@PathVariable Long id,
                                        @RequestParam double seminar,
                                        @RequestParam double activity,
                                        @RequestParam double paper) {
        return service.updateScores(id, seminar, activity, paper);
    }

    // ---- Test results ----
    @GetMapping("/test-results")
    public List<TestResult> getTestResults(@RequestParam Long studentId) {
        return service.getReleasedTestResults(studentId);
    }

    @PostMapping("/test-results")
    public ResponseEntity<TestResult> createTestResult(@RequestBody TestResult result) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveTestResult(result));
    }

    @PutMapping("/test-results/{id}/release")
    public TestResult releaseTestResult(@PathVariable Long id) {
        return service.releaseTestResult(id);
    }

    // ---- Email notifications ----
    @GetMapping("/notifications")
    public List<EmailNotification> getNotifications(@RequestParam Long studentId) {
        return service.getNotifications(studentId);
    }

    @PutMapping("/notifications")
    public EmailNotification toggle(@RequestParam Long studentId, @RequestParam Long courseId, @RequestParam boolean enabled) {
        return service.toggleNotification(studentId, courseId, enabled);
    }
}
