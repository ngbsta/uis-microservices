package com.uis.lectures.controller;

import com.uis.lectures.domain.*;
import com.uis.lectures.dto.ExamSittingDTO;
import com.uis.lectures.dto.StudentDTO;
import com.uis.lectures.dto.TestResultDTO;
import com.uis.lectures.service.LecturesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API of the My Lectures Sheet service (port 8083): courses, weekly timetable,
 * attendance (weeks 1-12), mid-term test results (score + activity point), course
 * materials and a per-course e-mail-notification toggle. Pulls a course's exam sittings
 * from exam-registration (8081) and the student list from e-study-record (8082).
 */
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

    // Inter-service: a course's exam sittings, fetched from exam-registration-service
    @GetMapping("/courses/{id}/exam-sittings")
    public List<ExamSittingDTO> getExamSittings(@PathVariable Long id) { return service.getExamSittings(id); }

    @GetMapping("/teachers")
    public List<Teacher> getTeachers() { return service.getTeachers(); }

    // Inter-service: student list, fetched from e-study-record-service
    @GetMapping("/students")
    public List<StudentDTO> getStudents() { return service.getStudents(); }

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

    // ---- Attendance (weekly present/absent — the core of My Lectures Sheet) ----
    @GetMapping("/courses/{id}/attendance")
    public List<Attendance> getAttendance(@PathVariable Long id, @RequestParam Long studentId) {
        return service.getAttendance(studentId, id);
    }

    // All courses' attendance for a student (used by the per-course matrix view)
    @GetMapping("/attendance")
    public List<Attendance> getAttendanceByStudent(@RequestParam Long studentId) {
        return service.getAttendanceByStudent(studentId);
    }

    @PostMapping("/attendance")
    public ResponseEntity<Attendance> markAttendance(@RequestBody Attendance attendance) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.markAttendance(attendance));
    }

    // ---- Mid-term test results ----
    @GetMapping("/test-results")
    public List<TestResultDTO> getTestResults(@RequestParam Long studentId,
                                              @RequestParam(required = false) Long courseId) {
        return courseId == null
                ? service.getReleasedTestResults(studentId)
                : service.getReleasedTestResults(studentId, courseId);
    }

    @PostMapping("/test-results")
    public ResponseEntity<TestResult> createTestResult(@RequestBody TestResult result) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveTestResult(result));
    }

    // ---- Email notifications: per-course "set send notifications" toggle ----
    @GetMapping("/notifications")
    public List<EmailNotification> getNotifications(@RequestParam Long studentId) {
        return service.getNotifications(studentId);
    }

    @PutMapping("/notifications")
    public EmailNotification toggle(@RequestParam Long studentId, @RequestParam Long courseId, @RequestParam boolean enabled) {
        return service.toggleNotification(studentId, courseId, enabled);
    }
}
