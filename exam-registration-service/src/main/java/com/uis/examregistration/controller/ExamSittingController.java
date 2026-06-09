package com.uis.examregistration.controller;

import com.uis.examregistration.dto.SittingDTO;
import com.uis.examregistration.dto.SittingRequest;
import com.uis.examregistration.service.ExamSittingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for exam sittings (Register for Examination, port 8081).
 * Students read sittings; teachers create/edit/delete them. All responses are DTOs.
 */
@RestController
@RequestMapping("/api/sittings")
public class ExamSittingController {

    private final ExamSittingService sittingService;

    public ExamSittingController(ExamSittingService sittingService) {
        this.sittingService = sittingService;
    }

    // List all sittings (each carries its course name, fetched from lectures-service)
    @GetMapping
    public List<SittingDTO> getAll() {
        return sittingService.getAll();
    }

    // One sitting by id
    @GetMapping("/{id}")
    public SittingDTO getById(@PathVariable Long id) {
        return sittingService.getById(id);
    }

    // Teacher: publish a new exam sitting
    @PostMapping
    public ResponseEntity<SittingDTO> create(@RequestBody SittingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sittingService.create(request));
    }

    // Teacher: edit an exam sitting
    @PutMapping("/{id}")
    public SittingDTO update(@PathVariable Long id, @RequestBody SittingRequest request) {
        return sittingService.update(id, request);
    }

    // Teacher: delete an exam sitting
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sittingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
