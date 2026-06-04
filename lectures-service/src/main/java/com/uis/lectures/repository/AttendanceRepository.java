package com.uis.lectures.repository;

import com.uis.lectures.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentIdAndCourseIdOrderByWeekAsc(Long studentId, Long courseId);
    List<Attendance> findByStudentIdOrderByWeekAsc(Long studentId);
}
