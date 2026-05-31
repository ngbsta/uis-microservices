package com.uis.examregistration.repository;

import com.uis.examregistration.domain.VacancyMonitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyMonitorRepository extends JpaRepository<VacancyMonitor, Long> {
    List<VacancyMonitor> findByStudentId(Long studentId);
    List<VacancyMonitor> findBySittingId(Long sittingId);
    boolean existsByStudentIdAndSittingId(Long studentId, Long sittingId);
}
