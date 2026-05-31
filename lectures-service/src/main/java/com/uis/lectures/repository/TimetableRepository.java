package com.uis.lectures.repository;

import com.uis.lectures.domain.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    Optional<Timetable> findByCourseId(Long courseId);
}
