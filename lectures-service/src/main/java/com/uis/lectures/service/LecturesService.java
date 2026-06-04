package com.uis.lectures.service;

import com.uis.lectures.client.ExamRegistrationClient;
import com.uis.lectures.client.StudyRecordClient;
import com.uis.lectures.domain.*;
import com.uis.lectures.dto.ExamSittingDTO;
import com.uis.lectures.dto.StudentDTO;
import com.uis.lectures.dto.TestResultDTO;
import com.uis.lectures.exception.BadRequestException;
import com.uis.lectures.exception.NotFoundException;
import com.uis.lectures.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LecturesService {

    /** A semester has exactly 12 teaching weeks; attendance cannot exceed this. */
    public static final int SEMESTER_WEEKS = 12;

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final CourseMaterialRepository materialRepository;
    private final TeacherRepository teacherRepository;
    private final TimetableRepository timetableRepository;
    private final AttendanceRepository attendanceRepository;
    private final TestResultRepository testResultRepository;
    private final CourseFolderRepository courseFolderRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final ExamRegistrationClient examRegistrationClient;
    private final StudyRecordClient studyRecordClient;

    public LecturesService(CourseRepository courseRepository, LectureRepository lectureRepository,
                           CourseMaterialRepository materialRepository, TeacherRepository teacherRepository,
                           TimetableRepository timetableRepository, AttendanceRepository attendanceRepository,
                           TestResultRepository testResultRepository, CourseFolderRepository courseFolderRepository,
                           EmailNotificationRepository emailNotificationRepository,
                           ExamRegistrationClient examRegistrationClient,
                           StudyRecordClient studyRecordClient) {
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
        this.materialRepository = materialRepository;
        this.teacherRepository = teacherRepository;
        this.timetableRepository = timetableRepository;
        this.attendanceRepository = attendanceRepository;
        this.testResultRepository = testResultRepository;
        this.courseFolderRepository = courseFolderRepository;
        this.emailNotificationRepository = emailNotificationRepository;
        this.examRegistrationClient = examRegistrationClient;
        this.studyRecordClient = studyRecordClient;
    }

    // ---- Courses / lectures / teachers ----
    public List<Course> getCourses() { return courseRepository.findAll(); }

    /** Inter-service: student list, fetched from e-study-record-service. */
    public List<StudentDTO> getStudents() {
        return studyRecordClient.getStudents();
    }

    /** Inter-service: exam sittings for a course, fetched from exam-registration-service. */
    public List<ExamSittingDTO> getExamSittings(Long courseId) {
        getCourse(courseId); // 404 if the course does not exist here
        return examRegistrationClient.getSittingsForCourse(courseId);
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
    }

    public List<Lecture> getLectures(Long courseId) { return lectureRepository.findByCourseId(courseId); }

    public List<Teacher> getTeachers() { return teacherRepository.findAll(); }

    /** Admin use case: reassign course teacher. */
    public Course reassignTeacher(Long courseId, Long teacherId) {
        Course course = getCourse(courseId);
        course.setTeacherId(teacherId);
        return courseRepository.save(course);
    }

    // ---- Timetable (student: view course timetable) ----
    public Timetable getTimetable(Long courseId) {
        return timetableRepository.findByCourseId(courseId)
                .orElseThrow(() -> new NotFoundException("Timetable not found for course: " + courseId));
    }

    // ---- Course folder + materials (upload / access documents) ----
    public CourseFolder getFolder(Long courseId) {
        return courseFolderRepository.findByCourseId(courseId)
                .orElseThrow(() -> new NotFoundException("Course folder not found for course: " + courseId));
    }

    public List<CourseMaterial> getMaterials(Long courseId) { return materialRepository.findByCourseId(courseId); }

    public CourseMaterial uploadMaterial(CourseMaterial material) { return materialRepository.save(material); }

    // ---- Attendance (student: view weekly present/absent; teacher: mark) ----
    // NOTE: the FINAL exam grade is NOT here — it lives in e-study-record-service.
    // My Lectures Sheet only tracks attendance + (mid-term) test results.
    public List<Attendance> getAttendance(Long studentId, Long courseId) {
        return attendanceRepository.findByStudentIdAndCourseIdOrderByWeekAsc(studentId, courseId);
    }

    /** All attendance for a student across every course (for the per-course matrix view). */
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentIdOrderByWeekAsc(studentId);
    }

    public Attendance markAttendance(Attendance a) {
        if (a.getWeek() < 1 || a.getWeek() > SEMESTER_WEEKS) {
            throw new BadRequestException("Week must be between 1 and " + SEMESTER_WEEKS + " (a semester has " + SEMESTER_WEEKS + " weeks).");
        }
        return attendanceRepository.save(a);
    }

    // ---- Test results (teacher: release; student: view released) ----
    // Enriched with real course + student names (student name fetched from e-study-record).
    public List<TestResultDTO> getReleasedTestResults(Long studentId) {
        return toTestDtos(testResultRepository.findByStudentIdAndReleasedTrue(studentId));
    }

    /** Released test results for ONE course (student clicks "Test results" on a course row). */
    public List<TestResultDTO> getReleasedTestResults(Long studentId, Long courseId) {
        return toTestDtos(testResultRepository.findByStudentIdAndCourseIdAndReleasedTrue(studentId, courseId));
    }

    private List<TestResultDTO> toTestDtos(List<TestResult> results) {
        Map<Long, String> courseNames = courseRepository.findAll().stream()
                .collect(Collectors.toMap(Course::getId, Course::getName));
        Map<Long, String> studentNames = studyRecordClient.getStudents().stream()
                .collect(Collectors.toMap(StudentDTO::getId, StudentDTO::getName));
        return results.stream()
                .map(r -> {
                    TestResultDTO d = new TestResultDTO();
                    d.setId(r.getId());
                    d.setCourseId(r.getCourseId());
                    d.setCourseName(courseNames.get(r.getCourseId()));
                    d.setStudentId(r.getStudentId());
                    d.setStudentName(studentNames.get(r.getStudentId()));
                    d.setScore(r.getScore());
                    d.setActivityPoint(r.getActivityPoint());
                    d.setReleased(r.isReleased());
                    d.setSubmittedAt(r.getSubmittedAt());
                    return d;
                })
                .toList();
    }

    public TestResult saveTestResult(TestResult result) { return testResultRepository.save(result); }

    // ---- Email notifications (per student+course "set send notifications" toggle) ----
    public List<EmailNotification> getNotifications(Long studentId) {
        return emailNotificationRepository.findByStudentId(studentId);
    }

    public EmailNotification toggleNotification(Long studentId, Long courseId, boolean enabled) {
        EmailNotification n = emailNotificationRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElse(new EmailNotification(studentId, courseId, enabled));
        n.setEnabled(enabled);
        return emailNotificationRepository.save(n);
    }
}
