package com.uis.examregistration.repository;

import com.uis.examregistration.domain.ExamRegistration;
import com.uis.examregistration.domain.ExamSitting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRegistrationRepository extends JpaRepository<ExamRegistration, Long> {
    List<ExamRegistration> findByStudentId(Long studentId);
    boolean existsByStudentIdAndExamSitting(Long studentId, ExamSitting examSitting);
    void deleteByExamSitting(ExamSitting examSitting);
}
