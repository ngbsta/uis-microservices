package com.uis.examregistration.service;

import com.uis.examregistration.client.EnrollmentClient;
import com.uis.examregistration.domain.ExamRegistration;
import com.uis.examregistration.domain.ExamSitting;
import com.uis.examregistration.domain.RegStatus;
import com.uis.examregistration.dto.RegistrationDTO;
import com.uis.examregistration.dto.RegistrationRequest;
import com.uis.examregistration.exception.CapacityFullException;
import com.uis.examregistration.exception.NotFoundException;
import com.uis.examregistration.exception.RegistrationException;
import com.uis.examregistration.domain.VacancyMonitor;
import com.uis.examregistration.repository.ExamRegistrationRepository;
import com.uis.examregistration.repository.ExamSittingRepository;
import com.uis.examregistration.repository.VacancyMonitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamRegistrationServiceImpl implements ExamRegistrationService {

    private final ExamRegistrationRepository registrationRepository;
    private final ExamSittingRepository sittingRepository;
    private final VacancyMonitorRepository monitorRepository;
    private final EnrollmentClient enrollmentClient;
    private final DtoMapper mapper;

    public ExamRegistrationServiceImpl(ExamRegistrationRepository registrationRepository,
                                       ExamSittingRepository sittingRepository,
                                       VacancyMonitorRepository monitorRepository,
                                       EnrollmentClient enrollmentClient,
                                       DtoMapper mapper) {
        this.registrationRepository = registrationRepository;
        this.sittingRepository = sittingRepository;
        this.monitorRepository = monitorRepository;
        this.enrollmentClient = enrollmentClient;
        this.mapper = mapper;
    }

    /**
     * Core "Register for an exam sitting" flow — same logic as the sequence /
     * activity diagrams: validate window, prerequisite (enrolment, checked via
     * e-study-record-service), duplicate, capacity; then store and increment.
     */
    @Override
    public RegistrationDTO register(RegistrationRequest request) {
        ExamSitting sitting = sittingRepository.findById(request.getSittingId())
                .orElseThrow(() -> new NotFoundException("Exam sitting not found: " + request.getSittingId()));

        // Order matches the Register-for-Examination activity diagram:
        // 1) registration window  2) free capacity  3) prerequisites (not already registered + enrolled)
        if (!sitting.isRegistrationOpen()) {
            throw new RegistrationException("Registration window is not open for this sitting.");
        }
        if (!sitting.hasFreeCapacity()) {
            throw new CapacityFullException("Capacity is full for this sitting.");
        }
        if (registrationRepository.existsByStudentIdAndExamSitting(request.getStudentId(), sitting)) {
            throw new RegistrationException("Student is already registered for this sitting.");
        }
        // inter-service call -> e-study-record-service (enrolment prerequisite)
        if (!enrollmentClient.isEnrolled(request.getStudentId(), sitting.getCourseId())) {
            throw new RegistrationException("Prerequisite missing: student is not enrolled in the course.");
        }

        ExamRegistration registration = new ExamRegistration(request.getStudentId(), sitting, RegStatus.REGISTERED);
        sitting.incrementCount();
        sittingRepository.save(sitting);
        ExamRegistration saved = registrationRepository.save(registration);
        return mapper.toDto(saved);
    }

    @Override
    public void unregister(Long registrationId) {
        ExamRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new NotFoundException("Registration not found: " + registrationId));
        ExamSitting sitting = registration.getExamSitting();
        if (!sitting.isUnregisterOpen()) {
            throw new RegistrationException("Unregistration deadline has passed.");
        }
        sitting.decrementCount();
        sittingRepository.save(sitting);
        registrationRepository.delete(registration);

        // UC-07: a place freed up -> alert students monitoring this sitting
        List<VacancyMonitor> monitors = monitorRepository.findBySittingId(sitting.getId());
        monitors.forEach(m -> m.setNotified(true));
        monitorRepository.saveAll(monitors);
    }

    @Override
    public RegistrationDTO getById(Long id) {
        ExamRegistration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registration not found: " + id));
        return mapper.toDto(registration);
    }

    @Override
    public List<RegistrationDTO> getByStudent(Long studentId) {
        return registrationRepository.findByStudentId(studentId).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<RegistrationDTO> getAll() {
        return registrationRepository.findAll().stream().map(mapper::toDto).toList();
    }
}
