package com.uis.estudyrecord.bootstrap;

import com.uis.estudyrecord.domain.Enrollment;
import com.uis.estudyrecord.domain.ExamResult;
import com.uis.estudyrecord.domain.Student;
import com.uis.estudyrecord.domain.StudyPeriod;
import com.uis.estudyrecord.repository.DatabaseConn;
import com.uis.estudyrecord.repository.EnrollmentRepository;
import com.uis.estudyrecord.repository.ExamResultRepository;
import com.uis.estudyrecord.repository.StudentRepository;
import com.uis.estudyrecord.service.StudyRecordService;
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

    public DataSeeder(StudentRepository studentRepository, StudyPeriodRepository periodRepository,
                      EnrollmentRepository enrollmentRepository, ExamResultRepository resultRepository) {
        this.studentRepository = studentRepository;
        this.periodRepository = periodRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.resultRepository = resultRepository;
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

        Student[] students = {s1, s2, s3};
        // course id -> credits (matches the lectures-service catalogue)
        int[] credits = {5, 6, 6, 6, 5};
        // Seeded overall scores [student][course] (mid-term + final, out of 100).
        int[][] overall = {
                {94, 100, 91, 98, 96},
                {82, 78, 85, 80, 76},
                {74, 68, 72, 70, 66},
        };

        for (int si = 0; si < students.length; si++) {
            Student st = students[si];
            for (long courseId = 1; courseId <= 5; courseId++) {
                int ci = (int) (courseId - 1);
                enrollmentRepository.save(new Enrollment(st.getId(), courseId, 1L, "ACTIVE"));
                double o = overall[si][ci];
                double midterm = o / 2.0;          // mid-term component (out of 50)
                double finalScore = o / 2.0;       // final component (out of 50)
                String grade = StudyRecordService.gradeFor(o);
                resultRepository.save(new ExamResult(st.getId(), courseId, null,
                        midterm, finalScore, grade, credits[ci], LocalDate.of(2026, 1, 20 + ci)));
            }
        }

        System.out.println(">>> e-study-record seeded: 3 students, all passing 5 courses. Singleton: "
                + DatabaseConn.getInstance().getConn());
    }
}
