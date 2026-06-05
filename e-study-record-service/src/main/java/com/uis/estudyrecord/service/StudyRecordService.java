package com.uis.estudyrecord.service;

import com.uis.estudyrecord.client.LecturesClient;
import com.uis.estudyrecord.domain.Enrollment;
import com.uis.estudyrecord.domain.ExamResult;
import com.uis.estudyrecord.domain.Student;
import com.uis.estudyrecord.dto.CourseDTO;
import com.uis.estudyrecord.dto.ExamResultDTO;
import com.uis.estudyrecord.dto.ResultRequest;
import com.uis.estudyrecord.exception.NotFoundException;
import com.uis.estudyrecord.repository.EnrollmentRepository;
import com.uis.estudyrecord.repository.ExamResultRepository;
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
    private final LecturesClient lecturesClient;

    public StudyRecordService(StudentRepository studentRepository,
                              EnrollmentRepository enrollmentRepository,
                              ExamResultRepository resultRepository,
                              LecturesClient lecturesClient) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.resultRepository = resultRepository;
        this.lecturesClient = lecturesClient;
    }

    // ---- Course catalogue (inter-service: fetched from lectures-service) ----
    public List<CourseDTO> getCourses() {
        return lecturesClient.getCourses();
    }

    /** Map an exam result to a DTO, attaching the course name from lectures-service. */
    private ExamResultDTO toDto(ExamResult r, Map<Long, String> courseNames) {
        ExamResultDTO dto = new ExamResultDTO();
        dto.setId(r.getId());
        dto.setStudentId(r.getStudentId());
        dto.setCourseId(r.getCourseId());
        dto.setCourseName(r.getCourseId() == null ? null
                : courseNames.getOrDefault(r.getCourseId(), "Course " + r.getCourseId()));
        dto.setSittingId(r.getSittingId());
        dto.setMidtermScore(r.getMidtermScore());
        dto.setFinalScore(r.getFinalScore());
        dto.setOverall(Math.round(r.overall() * 10.0) / 10.0);
        dto.setGrade(r.getGrade());
        dto.setCredits(r.getCredits());
        dto.setDate(r.getDate());
        return dto;
    }

    private List<ExamResultDTO> toDtos(List<ExamResult> results) {
        Map<Long, String> courseNames = lecturesClient.courseNames();
        return results.stream().map(r -> toDto(r, courseNames)).toList();
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
    public List<ExamResultDTO> getResults(Long studentId) {
        return toDtos(resultRepository.findByStudentId(studentId));
    }

    /** Credits are FIXED per course (owned by lectures-service), not set by the teacher. */
    private int creditsForCourse(Long courseId) {
        Integer credits = lecturesClient.courseCredits().get(courseId);
        return credits != null ? credits : 0;
    }

    /** Letter grade from the overall score = mid-term(/50) + final(/50), out of 100. */
    public static String gradeFor(double overall) {
        if (overall >= 90) return "A";
        if (overall >= 80) return "B";
        if (overall >= 70) return "C";
        if (overall >= 60) return "D";
        return "F";
    }

    /**
     * Teacher: enter a new exam result. The teacher provides only the FINAL exam score;
     * the MID-TERM is fetched from lectures-service (8083), the overall grade (A-F) is
     * computed from both, and credits come from the course. This makes the overall grade
     * in E-Study Record depend directly on the mid-term held by My Lectures Sheet.
     */
    /** Mid-term contributes 50% — the lectures test score (0-100) is taken as out of 50. */
    private double midtermComponent(Long studentId, Long courseId) {
        return Math.min(lecturesClient.midtermScore(studentId, courseId), 100.0) / 2.0;
    }
    /** Final exam contributes 50% — clamp the teacher's input to 0-50. */
    private double finalComponent(Double finalScore) {
        double f = finalScore != null ? finalScore : 0.0;
        return Math.max(0.0, Math.min(f, 50.0));
    }

    public ExamResult addResult(ResultRequest req) {
        double midterm = midtermComponent(req.getStudentId(), req.getCourseId());
        double finalScore = finalComponent(req.getFinalScore());
        ExamResult r = new ExamResult(
                req.getStudentId(), req.getCourseId(), req.getSittingId(),
                midterm, finalScore, gradeFor(midterm + finalScore),
                creditsForCourse(req.getCourseId()),
                req.getDate() != null ? req.getDate() : java.time.LocalDate.now());
        return resultRepository.save(r);
    }

    /** Teacher: update an exam result (new FINAL score). Mid-term re-fetched, grade recomputed. */
    public ExamResult updateResult(Long id, double finalScore) {
        ExamResult r = resultRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Result not found: " + id));
        double midterm = midtermComponent(r.getStudentId(), r.getCourseId());
        double fin = finalComponent(finalScore);
        r.setMidtermScore(midterm);
        r.setFinalScore(fin);
        r.setGrade(gradeFor(midterm + fin));
        r.setCredits(creditsForCourse(r.getCourseId()));
        return resultRepository.save(r);
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
        overview.put("examHistory", toDtos(results));
        overview.put("totalCredits", results.stream().mapToInt(ExamResult::getCredits).sum());
        overview.put("examsTaken", results.size());
        return overview;
    }
}
