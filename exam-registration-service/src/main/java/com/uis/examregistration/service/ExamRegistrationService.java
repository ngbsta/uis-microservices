package com.uis.examregistration.service;

import com.uis.examregistration.dto.RegistrationDTO;
import com.uis.examregistration.dto.RegistrationRequest;

import java.util.List;

public interface ExamRegistrationService {
    RegistrationDTO register(RegistrationRequest request);
    void unregister(Long registrationId);
    RegistrationDTO getById(Long id);
    List<RegistrationDTO> getByStudent(Long studentId);
    List<RegistrationDTO> getAll();
}
