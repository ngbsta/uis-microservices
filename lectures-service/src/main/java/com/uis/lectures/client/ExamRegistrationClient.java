package com.uis.lectures.client;

import com.uis.lectures.dto.ExamSittingDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Inter-service communication: lectures-service asks exam-registration-service
 * which exam sittings exist for a given course, so the "My Lectures Sheet"
 * module can show a course's exam sittings alongside its timetable.
 *
 * Uses Spring {@link RestTemplate}; the response is a list of DTOs.
 */
@Component
public class ExamRegistrationClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ExamRegistrationClient(RestTemplate restTemplate,
                                  @Value("${examregistration.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /** Fetch all sittings from exam-registration and keep only the given course's. */
    public List<ExamSittingDTO> getSittingsForCourse(Long courseId) {
        List<ExamSittingDTO> all = restTemplate.exchange(
                baseUrl + "/api/sittings",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExamSittingDTO>>() {}
        ).getBody();

        if (all == null) {
            return Collections.emptyList();
        }
        return all.stream()
                .filter(s -> courseId.equals(s.getCourseId()))
                .toList();
    }
}
