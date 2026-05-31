package com.uis.estudyrecord.repository;

import com.uis.estudyrecord.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
