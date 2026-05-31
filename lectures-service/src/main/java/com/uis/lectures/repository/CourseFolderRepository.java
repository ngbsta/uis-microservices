package com.uis.lectures.repository;

import com.uis.lectures.domain.CourseFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseFolderRepository extends JpaRepository<CourseFolder, Long> {
    Optional<CourseFolder> findByCourseId(Long courseId);
}
