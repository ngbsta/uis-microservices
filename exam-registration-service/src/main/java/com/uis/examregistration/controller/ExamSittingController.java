package com.uis.examregistration.controller;

import com.uis.examregistration.dto.SittingDTO;
import com.uis.examregistration.dto.SittingRequest;
import com.uis.examregistration.service.ExamSittingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sittings")
public class ExamSittingController {

    private final ExamSittingService sittingService;

    public ExamSittingController(ExamSittingService sittingService) {
        this.sittingService = sittingService;
    }

    @GetMapping
    public List<SittingDTO> getAll() {
        return sittingService.getAll();
    }

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
