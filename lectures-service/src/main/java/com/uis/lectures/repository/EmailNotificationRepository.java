package com.uis.lectures.repository;

import com.uis.lectures.domain.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    List<EmailNotification> findByStudentId(Long studentId);
    Optional<EmailNotification> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
