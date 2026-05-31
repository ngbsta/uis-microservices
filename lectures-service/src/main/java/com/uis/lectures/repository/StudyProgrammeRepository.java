package com.uis.lectures.repository;

import com.uis.lectures.domain.StudyProgramme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProgrammeRepository extends JpaRepository<StudyProgramme, Long> {
}
