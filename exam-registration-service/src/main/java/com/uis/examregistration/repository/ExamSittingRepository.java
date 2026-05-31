package com.uis.examregistration.repository;

import com.uis.examregistration.domain.ExamSitting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamSittingRepository extends JpaRepository<ExamSitting, Long> {
    List<ExamSitting> findByCourseId(Long courseId);
}
