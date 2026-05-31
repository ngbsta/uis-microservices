package com.uis.estudyrecord.service;

import com.uis.estudyrecord.domain.Enrollment;
import com.uis.estudyrecord.domain.ExamResult;
import com.uis.estudyrecord.domain.Notification;
import com.uis.estudyrecord.domain.Student;
import com.uis.estudyrecord.exception.NotFoundException;
import com.uis.estudyrecord.repository.EnrollmentRepository;
import com.uis.estudyrecord.repository.ExamResultRepository;
import com.uis.estudyrecord.repository.NotificationRepository;
import com.uis.estudyrecord.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StudyRecordService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ExamResultRepository resultRepository;
    private final NotificationRepository notificationRepository;

    public StudyRecordService(StudentRepository studentRepository,
                              EnrollmentRepository enrollmentRepository,
                              ExamResultRepository resultRepository,
                              NotificationRepository notificationRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.resultRepository = resultRepository;
        this.notificationRepository = notificationRepository;
    }

    // ---- Notifications (Notification class; Student 1 -- 0..* Notification) ----
    public List<Notification> getNotifications(Long studentId) {
        return notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    public Notification markNotificationRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found: " + id));
        n.markAsRead();
        return notificationRepository.save(n);
    }

    // ---- Students (search & select) ----
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found: " + id));
    }

    // ---- Enrolments ----
    public List<Enrollment> getEnrollments(Long studentId) {
        return studentId == null ? enrollmentRepository.findAll()
                : enrollmentRepository.findByStudentId(studentId);
    }

    /** Used by exam-registration-service to verify a prerequisite before registration. */
    public boolean isEnrolled(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    public Enrollment addEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    // ---- Exam results / history ----
    public List<ExamResult> getResults(Long studentId) {
        return resultRepository.findByStudentId(studentId);
    }

    /** Teacher: enter a new exam result. Notifies the student (BPMN "Notify Student Results"). */
    public ExamResult addResult(ExamResult result) {
        ExamResult saved = resultRepository.save(result);
        notificationRepository.save(new Notification(saved.getStudentId(),
                "Your exam result has been published: grade " + saved.getGrade()
                        + " (" + saved.getCredits() + " credits)."));
        return saved;
    }

    /** Teacher: update an exam result (grade, credits awarded). Notifies the student. */
    public ExamResult updateResult(Long id, String grade, int attempt, int credits) {
        ExamResult r = resultRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Result not found: " + id));
        r.setGrade(grade);
        r.setAttempt(attempt);
        r.setCredits(credits);
        ExamResult saved = resultRepository.save(r);
        notificationRepository.save(new Notification(saved.getStudentId(),
                "Your exam result has been updated: grade " + saved.getGrade() + "."));
        return saved;
    }

    /** Student: track credits obtained (sum over results). */
    public int getTotalCredits(Long studentId) {
        return resultRepository.findByStudentId(studentId).stream()
                .mapToInt(ExamResult::getCredits).sum();
    }

    /** Student: study overview (basic + whole study) — enrolments, results, credits. */
    public Map<String, Object> getStudyOverview(Long studentId) {
        Student student = getStudent(studentId);
        List<Enrollment> enrolments = enrollmentRepository.findByStudentId(studentId);
        List<ExamResult> results = resultRepository.findByStudentId(studentId);
        Map<String, Object> overview = new LinkedHashMap<>();
        overview.put("student", student);
        overview.put("enrolments", enrolments);
        overview.put("examHistory", results);
        overview.put("totalCredits", results.stream().mapToInt(ExamResult::getCredits).sum());
        overview.put("examsTaken", results.size());
        return overview;
    }
}
