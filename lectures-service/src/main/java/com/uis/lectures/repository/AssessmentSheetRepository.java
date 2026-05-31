package com.uis.lectures.repository;

import com.uis.lectures.domain.AssessmentSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentSheetRepository extends JpaRepository<AssessmentSheet, Long> {
    List<AssessmentSheet> findByStudentId(Long studentId);
    List<AssessmentSheet> findByCourseId(Long courseId);
}
