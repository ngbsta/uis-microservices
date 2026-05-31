package com.uis.examregistration.service;

import com.uis.examregistration.dto.SittingDTO;
import com.uis.examregistration.dto.SittingRequest;

import java.util.List;

public interface ExamSittingService {
    List<SittingDTO> getAll();
    SittingDTO getById(Long id);
    SittingDTO create(SittingRequest request);
    SittingDTO update(Long id, SittingRequest request);
    void delete(Long id);
}
