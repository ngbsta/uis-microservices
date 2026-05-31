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
        // OPEN sitting with free capacity (course id 1, teacher id 1)
        sittingRepository.save(new ExamSitting(
                LocalDateTime.now().plusDays(20), "Q01", "Written", 30,
                LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(15), 1L, 1L));

        // Sitting whose registration window has not opened yet
        sittingRepository.save(new ExamSitting(
                LocalDateTime.now().plusDays(40), "Q02", "Oral", 15,
                LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(25),
                LocalDateTime.now().plusDays(30), 1L, 1L));

        System.out.println(">>> exam-registration seeded. Singleton: " + DatabaseConn.getInstance().getConn());
    }
}
