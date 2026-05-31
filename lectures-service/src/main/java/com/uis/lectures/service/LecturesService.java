package com.uis.lectures.service;

import com.uis.lectures.domain.*;
import com.uis.lectures.exception.NotFoundException;
import com.uis.lectures.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LecturesService {

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final CourseMaterialRepository materialRepository;
    private final TeacherRepository teacherRepository;
    private final TimetableRepository timetableRepository;
    private final AssessmentSheetRepository assessmentSheetRepository;
    private final TestResultRepository testResultRepository;
    private final CourseFolderRepository courseFolderRepository;
    private final EmailNotificationRepository emailNotificationRepository;

    public LecturesService(CourseRepository courseRepository, LectureRepository lectureRepository,
                           CourseMaterialRepository materialRepository, TeacherRepository teacherRepository,
                           TimetableRepository timetableRepository, AssessmentSheetRepository assessmentSheetRepository,
                           TestResultRepository testResultRepository, CourseFolderRepository courseFolderRepository,
                           EmailNotificationRepository emailNotificationRepository) {
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
        this.materialRepository = materialRepository;
        this.teacherRepository = teacherRepository;
        this.timetableRepository = timetableRepository;
        this.assessmentSheetRepository = assessmentSheetRepository;
        this.testResultRepository = testResultRepository;
        this.courseFolderRepository = courseFolderRepository;
        this.emailNotificationRepository = emailNotificationRepository;
    }

    // ---- Courses / lectures / teachers ----
    public List<Course> getCourses() { return courseRepository.findAll(); }

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

    // ---- Assessment sheets (teacher: maintain / enter scores; student: view) ----
    public List<AssessmentSheet> getAssessmentSheetsByStudent(Long studentId) {
        return assessmentSheetRepository.findByStudentId(studentId);
    }

    public AssessmentSheet saveAssessmentSheet(AssessmentSheet sheet) {
        sheet.computeOverallGrade();   // define automatic assessment
        return assessmentSheetRepository.save(sheet);
    }

    public AssessmentSheet updateScores(Long id, double seminar, double activity, double paper) {
        AssessmentSheet sheet = assessmentSheetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assessment sheet not found: " + id));
        sheet.setSeminarScore(seminar);
        sheet.setActivityScore(activity);
        sheet.setPaperScore(paper);
        sheet.computeOverallGrade();
        return assessmentSheetRepository.save(sheet);
    }

    // ---- Test results (teacher: release; student: view released) ----
    public List<TestResult> getReleasedTestResults(Long studentId) {
        return testResultRepository.findByStudentIdAndReleasedTrue(studentId);
    }

    public TestResult saveTestResult(TestResult result) { return testResultRepository.save(result); }

    public TestResult releaseTestResult(Long id) {
        TestResult result = testResultRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Test result not found: " + id));
        result.setReleased(true);
        return testResultRepository.save(result);
    }

    // ---- Email notifications (student: toggle coursework alerts) ----
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
