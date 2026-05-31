package com.uis.examregistration.service;

import com.uis.examregistration.domain.ExamRegistration;
import com.uis.examregistration.domain.ExamSitting;
import com.uis.examregistration.dto.RegistrationDTO;
import com.uis.examregistration.dto.SittingDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Entity -> DTO conversion. Uses ModelMapper for the flat fields and sets
 * derived flags / foreign-key ids explicitly so the JSON stays clean.
 */
@Component
public class DtoMapper {

    private final ModelMapper modelMapper;

    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RegistrationDTO toDto(ExamRegistration r) {
        RegistrationDTO dto = modelMapper.map(r, RegistrationDTO.class);
        dto.setStatus(r.getStatus() != null ? r.getStatus().name() : null);
        dto.setStudentId(r.getStudentId());
        dto.setSittingId(r.getExamSitting() != null ? r.getExamSitting().getId() : null);
        return dto;
    }

    public SittingDTO toDto(ExamSitting s) {
        SittingDTO dto = modelMapper.map(s, SittingDTO.class);
        dto.setRegistrationOpen(s.isRegistrationOpen());
        dto.setFreeCapacity(s.hasFreeCapacity());
        return dto;
    }
}
