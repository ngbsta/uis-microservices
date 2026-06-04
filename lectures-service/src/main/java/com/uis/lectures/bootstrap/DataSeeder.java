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
    private final AttendanceRepository attendanceRepository;
    private final TestResultRepository testResultRepository;
    private final CourseFolderRepository courseFolderRepository;
    private final EmailNotificationRepository emailNotificationRepository;

    public DataSeeder(StudyProgrammeRepository programmeRepository, TeacherRepository teacherRepository,
                      CourseRepository courseRepository, LectureRepository lectureRepository,
                      CourseMaterialRepository materialRepository, TimetableRepository timetableRepository,
                      AttendanceRepository attendanceRepository, TestResultRepository testResultRepository,
                      CourseFolderRepository courseFolderRepository,
                      EmailNotificationRepository emailNotificationRepository) {
        this.programmeRepository = programmeRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
        this.materialRepository = materialRepository;
        this.timetableRepository = timetableRepository;
        this.attendanceRepository = attendanceRepository;
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
                {"EBC-SA",  "Software and Services Architectures", "6"},
                {"EBC-SD",  "Software and Services Deployment",   "6"},
                {"EBC-TM",  "Text Mining",          "5"},
        };
        String[] days  = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] rooms = {"Q01", "Q02", "Q03", "Q04", "Q05"};

        // My Lectures Sheet content per course: lecture, timetable, folder, material,
        // WEEKLY ATTENDANCE (weeks 1..12), a mid-term test result, notification — for student 1.
        // NOTE: no overall/final grade here — that belongs to e-study-record-service.
        int weeks = 12;
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
                    days[i] + " " + hour + ":00-" + (hour + 2) + ":00 " + rooms[i]));
            courseFolderRepository.save(new CourseFolder(courseId,
                    "/srv/courses/" + code, "teacher:rw; student:r"));

            // Weekly attendance for ALL THREE students — mostly present, a few absences /
            // excused, with a different pattern per student so the data looks realistic.
            long[] students = {1L, 2L, 3L};
            for (long sid : students) {
                for (int w = 1; w <= weeks; w++) {
                    String status;
                    if (sid == 1L)       status = (w == 3 + i || w == 9) ? "absent" : "present";
                    else if (sid == 2L)  status = (w == 2 || w == 11) ? "absent" : (w == 7 ? "excused" : "present");
                    else                 status = (w == 5 || w == 4 + i) ? "absent" : (w == 4 ? "excused" : "present");
                    attendanceRepository.save(new Attendance(sid, courseId, w, status));
                }
                // One released MID-TERM test result per student (partial score — NOT the final grade).
                double score = 70 + (sid * 5) + i;       // varied per student/course
                int activityPoint = (int) (5 + (sid + i) % 6);  // activity points (0–10ish), varied
                testResultRepository.save(new TestResult(courseId, sid, score, activityPoint, true,
                        LocalDateTime.now().minusDays(5)));

                // "Set send notifications" toggle per course — default OFF (student turns it on).
                emailNotificationRepository.save(new EmailNotification(sid, courseId, false));
            }
        }

        System.out.println(">>> lectures seeded: " + courses.length
                + " courses (Business Economics 2, Neural Networks, Software and Services Architectures, "
                + "Software and Services Deployment, Text Mining). Singleton: "
                + DatabaseConn.getInstance().getConn());
    }
}
