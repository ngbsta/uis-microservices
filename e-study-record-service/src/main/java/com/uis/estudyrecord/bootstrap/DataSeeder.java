package com.uis.estudyrecord.bootstrap;

import com.uis.estudyrecord.domain.Enrollment;
import com.uis.estudyrecord.domain.ExamResult;
import com.uis.estudyrecord.domain.Notification;
import com.uis.estudyrecord.domain.Student;
import com.uis.estudyrecord.domain.StudyPeriod;
import com.uis.estudyrecord.repository.DatabaseConn;
import com.uis.estudyrecord.repository.EnrollmentRepository;
import com.uis.estudyrecord.repository.ExamResultRepository;
import com.uis.estudyrecord.repository.NotificationRepository;
import com.uis.estudyrecord.repository.StudentRepository;
import com.uis.estudyrecord.repository.StudyPeriodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final StudyPeriodRepository periodRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ExamResultRepository resultRepository;
    private final NotificationRepository notificationRepository;

    public DataSeeder(StudentRepository studentRepository, StudyPeriodRepository periodRepository,
                      EnrollmentRepository enrollmentRepository, ExamResultRepository resultRepository,
                      NotificationRepository notificationRepository) {
        this.studentRepository = studentRepository;
        this.periodRepository = periodRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.resultRepository = resultRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() > 0) {
            return;
        }
        // Several students so the teacher can pick WHO to grade (id auto = 1,2,3)
        Student s1 = studentRepository.save(new Student("Kutay Tanriverdi", "xtanrive@mendelu.cz", LocalDate.of(2003, 5, 10), "C-OIA-ZNOIA"));
        Student s2 = studentRepository.save(new Student("Myat Noe Khin", "xmyatnoe@mendelu.cz", LocalDate.of(2002, 11, 3), "C-OIA-ZNOIA"));
        Student s3 = studentRepository.save(new Student("Muhammad Umer Ijaz", "xijaz@mendelu.cz", LocalDate.of(2003, 1, 22), "C-OIA-ZNOIA"));

        periodRepository.save(new StudyPeriod("SS 2025/2026", LocalDate.of(2026, 2, 1), LocalDate.of(2026, 6, 30)));

        // all enrolled in course 1
        enrollmentRepository.save(new Enrollment(s1.getId(), 1L, 1L, "ACTIVE"));
        enrollmentRepository.save(new Enrollment(s2.getId(), 1L, 1L, "ACTIVE"));
        enrollmentRepository.save(new Enrollment(s3.getId(), 1L, 1L, "ACTIVE"));

        // one existing graded result for student 1
        resultRepository.save(new ExamResult(s1.getId(), 1L, "B", 1, 6, LocalDate.of(2025, 6, 20)));

        // sample notification for student 1 (Student 1 -- 0..* Notification)
        notificationRepository.save(new Notification(s1.getId(),
                "Your exam result has been published: grade B (6 credits)."));

        System.out.println(">>> e-study-record seeded (3 students + notification). Singleton: " + DatabaseConn.getInstance().getConn());
    }
}
