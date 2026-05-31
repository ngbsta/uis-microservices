package com.uis.examregistration.controller;

import com.uis.examregistration.domain.VacancyMonitor;
import com.uis.examregistration.repository.VacancyMonitorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** UC-07: vacancy monitoring for full exam sittings. */
@RestController
@RequestMapping("/api/monitors")
public class VacancyMonitorController {

    private final VacancyMonitorRepository repo;

    public VacancyMonitorController(VacancyMonitorRepository repo) {
        this.repo = repo;
    }

    // Student starts watching a full sitting
    @PostMapping
    public ResponseEntity<?> watch(@RequestBody Map<String, Long> body) {
        Long studentId = body.get("studentId");
        Long sittingId = body.get("sittingId");
        if (repo.existsByStudentIdAndSittingId(studentId, sittingId)) {
            return ResponseEntity.ok(Map.of("message", "Already monitoring this sitting."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(new VacancyMonitor(studentId, sittingId)));
    }

    @GetMapping
    public List<VacancyMonitor> byStudent(@RequestParam Long studentId) {
        return repo.findByStudentId(studentId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> stop(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
