package com.uis.examregistration.service;

import com.uis.examregistration.client.LecturesClient;
import com.uis.examregistration.domain.ExamSitting;
import com.uis.examregistration.dto.SittingDTO;
import com.uis.examregistration.dto.SittingRequest;
import com.uis.examregistration.exception.NotFoundException;
import com.uis.examregistration.exception.RegistrationException;
import com.uis.examregistration.repository.ExamRegistrationRepository;
import com.uis.examregistration.repository.ExamSittingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExamSittingServiceImpl implements ExamSittingService {

    private final ExamSittingRepository sittingRepository;
    private final ExamRegistrationRepository registrationRepository;
    private final DtoMapper mapper;
    private final LecturesClient lecturesClient;

    public ExamSittingServiceImpl(ExamSittingRepository sittingRepository,
                                  ExamRegistrationRepository registrationRepository,
                                  DtoMapper mapper,
                                  LecturesClient lecturesClient) {
        this.sittingRepository = sittingRepository;
        this.registrationRepository = registrationRepository;
        this.mapper = mapper;
        this.lecturesClient = lecturesClient;
    }

    /** Attach the course name (resolved from lectures-service) to a DTO. */
    private SittingDTO withCourseName(SittingDTO dto, Map<Long, String> names) {
        dto.setCourseName(names.get(dto.getCourseId()));
        return dto;
    }

    @Override
    public List<SittingDTO> getAll() {
        Map<Long, String> names = lecturesClient.courseNames();   // one inter-service call
        return sittingRepository.findAll().stream()
                .map(mapper::toDto)
                .map(d -> withCourseName(d, names))
                .toList();
    }

    @Override
    public SittingDTO getById(Long id) {
        return withCourseName(mapper.toDto(find(id)), lecturesClient.courseNames());
    }

    /** Teacher: create / publish a new exam sitting. Dates default to an open window. */
    @Override
    public SittingDTO create(SittingRequest req) {
        LocalDateTime now = LocalDateTime.now();
        ExamSitting s = new ExamSitting(
                req.getDate() != null ? req.getDate() : now.plusDays(20),
                req.getRoom(), req.getType(), req.getCapacity(),
                req.getRegisterFrom() != null ? req.getRegisterFrom() : now.minusDays(1),
                req.getRegisterUntil() != null ? req.getRegisterUntil() : now.plusDays(14),
                req.getUnregisterUntil() != null ? req.getUnregisterUntil() : now.plusDays(18),
                req.getCourseId(), req.getTeacherId());
        validate(s);
        return withCourseName(mapper.toDto(sittingRepository.save(s)), lecturesClient.courseNames());
    }

    /** UC-06a: reject invalid input (capacity, wrong date order). */
    private void validate(ExamSitting s) {
        if (s.getCapacity() <= 0)
            throw new RegistrationException("Capacity must be greater than 0.");
        if (s.getRegisterFrom() != null && s.getRegisterUntil() != null
                && s.getRegisterFrom().isAfter(s.getRegisterUntil()))
            throw new RegistrationException("Registration 'from' must be before 'until'.");
        if (s.getRegisterUntil() != null && s.getDate() != null
                && s.getRegisterUntil().isAfter(s.getDate()))
            throw new RegistrationException("Registration must close before the exam date.");
        if (s.getUnregisterUntil() != null && s.getDate() != null
                && s.getUnregisterUntil().isAfter(s.getDate()))
            throw new RegistrationException("Unregistration deadline must be before the exam date.");
    }

    /** Teacher: edit an existing exam sitting (only non-null fields are changed). */
    @Override
    public SittingDTO update(Long id, SittingRequest req) {
        ExamSitting s = find(id);
        if (req.getCourseId() != null) s.setCourseId(req.getCourseId());
        if (req.getTeacherId() != null) s.setTeacherId(req.getTeacherId());
        if (req.getRoom() != null) s.setRoom(req.getRoom());
        if (req.getType() != null) s.setType(req.getType());
        if (req.getCapacity() > 0) s.setCapacity(req.getCapacity());
        if (req.getDate() != null) s.setDate(req.getDate());
        if (req.getRegisterFrom() != null) s.setRegisterFrom(req.getRegisterFrom());
        if (req.getRegisterUntil() != null) s.setRegisterUntil(req.getRegisterUntil());
        if (req.getUnregisterUntil() != null) s.setUnregisterUntil(req.getUnregisterUntil());
        validate(s);
        return withCourseName(mapper.toDto(sittingRepository.save(s)), lecturesClient.courseNames());
    }

    /** Teacher: delete an exam sitting (its registrations are removed first). */
    @Override
    public void delete(Long id) {
        ExamSitting s = find(id);
        registrationRepository.deleteByExamSitting(s);
        sittingRepository.delete(s);
    }

    private ExamSitting find(Long id) {
        return sittingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exam sitting not found: " + id));
    }
}
