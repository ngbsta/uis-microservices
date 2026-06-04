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
        // Programme id 1, Teacher id 1, Student id 1 (consistent ids across services)
        programmeRepository.save(new StudyProgramme("C-OIA-ZNOIA", "Open Informatics", "FBE"));
        teacherRepository.save(new Teacher("Oldrich Faldik", "faldik@mendelu.cz"));

        // The five courses (the "dummy lectures" list) — seeded from scratch as ids 1..5.
        String[][] courses = {
                {"EBC-BE2", "Business Economics 2", "5"},
                {"EBC-NN",  "Neural Networks",       "6"},
                {"EBC-SA",  "Software and Architecture", "6"},
                {"EBC-SD",  "Software and Deployment",   "6"},
                {"EBC-TM",  "Text Mining",          "5"},
        };
        String[] days  = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] rooms = {"Q01", "Q02", "Q03", "Q04", "Q05"};

        // Per-course "My Lectures Sheet" data (lecture, timetable, folder, material,
        // assessment sheet, test result, notification) for student 1.
        for (int i = 0; i < courses.length; i++) {
            String code = courses[i][0];
            String name = courses[i][1];
            int credits = Integer.parseInt(courses[i][2]);
            int hour = 9 + i;   // staggered start times

            Course course = courseRepository.save(
                    new Course(code, name, credits, "EN", true, 1L, 1L));
            Long courseId = course.getId();

            lectureRepository.save(new Lecture(courseId, days[i],
                    LocalTime.of(hour, 0), LocalTime.of(hour + 2, 0), rooms[i]));
            materialRepository.save(new CourseMaterial(courseId, name + " - Lecture 1 slides",
                    "/files/" + code.toLowerCase() + "/lec1.pdf", LocalDateTime.now()));
            timetableRepository.save(new Timetable(courseId,
                    days[i] + " " + hour + ":00-" + (hour + 2) + ":00 " + rooms[i],
                    "2026-03-02: present; 2026-03-09: present; 2026-03-16: absent"));
            courseFolderRepository.save(new CourseFolder(courseId,
                    "/srv/courses/" + code, "teacher:rw; student:r"));

            AssessmentSheet sheet = new AssessmentSheet(courseId, 1L, 28, 18, 30, null);
            sheet.computeOverallGrade();
            assessmentSheetRepository.save(sheet);

            testResultRepository.save(new TestResult(courseId, 1L, 78.5, true,
                    LocalDateTime.now().minusDays(5)));
            emailNotificationRepository.save(new EmailNotification(1L, courseId, true));
        }

        System.out.println(">>> lectures seeded: " + courses.length
                + " courses (Business Economics 2, Neural Networks, Software and Architecture, "
                + "Software and Deployment, Text Mining). Singleton: "
                + DatabaseConn.getInstance().getConn());
    }
}
