package com.uis.lectures.bootstrap;

import com.uis.lectures.domain.*;
import com.uis.lectures.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final StudyProgrammeRepository programmeRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final CourseMaterialRepository materialRepository;
    private final TimetableRepository timetableRepository;
    private final AssessmentSheetRepository assessmentSheetRepository;
    private final TestResultRepository testResultRepository;
    private final CourseFolderRepository courseFolderRepository;
    private final EmailNotificationRepository emailNotificationRepository;

    public DataSeeder(StudyProgrammeRepository programmeRepository, TeacherRepository teacherRepository,
                      CourseRepository courseRepository, LectureRepository lectureRepository,
                      CourseMaterialRepository materialRepository, TimetableRepository timetableRepository,
                      AssessmentSheetRepository assessmentSheetRepository, TestResultRepository testResultRepository,
                      CourseFolderRepository courseFolderRepository, EmailNotificationRepository emailNotificationRepository) {
        this.programmeRepository = programmeRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
        this.materialRepository = materialRepository;
        this.timetableRepository = timetableRepository;
        this.assessmentSheetRepository = assessmentSheetRepository;
        this.testResultRepository = testResultRepository;
        this.courseFolderRepository = courseFolderRepository;
        this.emailNotificationRepository = emailNotificationRepository;
    }

    @Override
    public void run(String... args) {
        if (courseRepository.count() > 0) {
            return;
        }
        // Programme id 1, Teacher id 1, Course id 1, Student id 1 (consistent ids across services)
        programmeRepository.save(new StudyProgramme("C-OIA-ZNOIA", "Open Informatics", "FBE"));
        teacherRepository.save(new Teacher("Oldrich Faldik", "faldik@mendelu.cz"));
        courseRepository.save(new Course("EBC-DSA", "Data Structures & Algorithms", 6, "EN", true, 1L, 1L));

        lectureRepository.save(new Lecture(1L, "Monday", LocalTime.of(9, 0), LocalTime.of(11, 0), "Q01"));
        materialRepository.save(new CourseMaterial(1L, "Lecture 1 - Slides", "/files/dsa/lec1.pdf", LocalDateTime.now()));

        // Umer's "My Lectures Sheet" data
        timetableRepository.save(new Timetable(1L,
                "Mon 09:00-11:00 Q01; Wed 13:00-15:00 Q02",
                "2026-03-02: present; 2026-03-09: present; 2026-03-16: absent"));
        courseFolderRepository.save(new CourseFolder(1L, "/srv/courses/EBC-DSA", "teacher:rw; student:r"));

        AssessmentSheet sheet = new AssessmentSheet(1L, 1L, 28, 18, 30, null);
        sheet.computeOverallGrade();
        assessmentSheetRepository.save(sheet);

        testResultRepository.save(new TestResult(1L, 1L, 78.5, true, LocalDateTime.now().minusDays(5)));
        emailNotificationRepository.save(new EmailNotification(1L, 1L, true));

        System.out.println(">>> lectures seeded (Umer's My Lectures Sheet). Singleton: "
                + DatabaseConn.getInstance().getConn());
    }
}
