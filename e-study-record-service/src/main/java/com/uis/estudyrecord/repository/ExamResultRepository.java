package com.uis.estudyrecord.repository;

import com.uis.estudyrecord.domain.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByStudentId(Long studentId);
}
