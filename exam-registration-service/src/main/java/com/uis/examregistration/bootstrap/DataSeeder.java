package com.uis.examregistration.bootstrap;

import com.uis.examregistration.domain.ExamSitting;
import com.uis.examregistration.repository.DatabaseConn;
import com.uis.examregistration.repository.ExamSittingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Seeds exam sittings on startup (only when empty). courseId / teacherId refer
 * to data owned by lectures-service (course id 1, teacher id 1).
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ExamSittingRepository sittingRepository;

    public DataSeeder(ExamSittingRepository sittingRepository) {
        this.sittingRepository = sittingRepository;
    }

    @Override
    public void run(String... args) {
        if (sittingRepository.count() > 0) {
            return;
        }
        // One OPEN sitting (free capacity) per course (ids 1..5, owned by lectures-service)
        String[] rooms = {"Q01", "Q02", "Q03", "Q04", "Q05"};
        for (long courseId = 1; courseId <= 5; courseId++) {
            sittingRepository.save(new ExamSitting(
                    LocalDateTime.now().plusDays(20), rooms[(int) (courseId - 1)], "Written", 30,
                    LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(10),
                    LocalDateTime.now().plusDays(15), courseId, 1L));
        }

        // Sitting whose registration window has not opened yet (course 1)
        sittingRepository.save(new ExamSitting(
                LocalDateTime.now().plusDays(40), "Q02", "Oral", 15,
                LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(25),
                LocalDateTime.now().plusDays(30), 1L, 1L));

        System.out.println(">>> exam-registration seeded. Singleton: " + DatabaseConn.getInstance().getConn());
    }
}
