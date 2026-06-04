package com.uis.lectures.repository;

import com.uis.lectures.domain.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByStudentId(Long studentId);
    List<TestResult> findByStudentIdAndReleasedTrue(Long studentId);
    List<TestResult> findByStudentIdAndCourseIdAndReleasedTrue(Long studentId, Long courseId);
}
