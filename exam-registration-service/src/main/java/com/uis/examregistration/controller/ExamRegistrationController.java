package com.uis.examregistration.controller;

import com.uis.examregistration.dto.RegistrationDTO;
import com.uis.examregistration.dto.RegistrationRequest;
import com.uis.examregistration.service.ExamRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for exam registrations (port 8081). A registration is only accepted if the
 * student is enrolled in the course — checked live against e-study-record (8082).
 * Unregistering frees a seat and alerts anyone monitoring that sitting. Responses are DTOs.
 */
@RestController
@RequestMapping("/api/registrations")
public class ExamRegistrationController {

    private final ExamRegistrationService registrationService;

    public ExamRegistrationController(ExamRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // Register for an exam sitting
    @PostMapping
    public ResponseEntity<RegistrationDTO> register(@Valid @RequestBody RegistrationRequest request) {
        RegistrationDTO dto = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // Get one registration by id
    @GetMapping("/{id}")
    public RegistrationDTO getById(@PathVariable Long id) {
        return registrationService.getById(id);
    }

    // All registrations (optionally filtered by student)
    @GetMapping
    public List<RegistrationDTO> getAll(@RequestParam(required = false) Long studentId) {
        return studentId == null
                ? registrationService.getAll()
                : registrationService.getByStudent(studentId);
    }

    // Unregister (delete) a registration by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unregister(@PathVariable Long id) {
        registrationService.unregister(id);
        return ResponseEntity.noContent().build();
    }
}
