package com.uis.examregistration.client;

import com.uis.examregistration.dto.EnrollmentStatusDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Inter-service communication: asks the e-study-record-service whether a student
 * is enrolled in a course before allowing an exam registration.
 * (Week 10 requirement: "each app is communicating with each other".)
 *
 * Uses Spring {@link RestTemplate} and deserializes the response into a DTO.
 */
@Component
public class EnrollmentClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public EnrollmentClient(RestTemplate restTemplate,
                            @Value("${estudyrecord.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public boolean isEnrolled(Long studentId, Long courseId) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/enrollments/exists")
                .queryParam("studentId", studentId)
                .queryParam("courseId", courseId)
                .toUriString();

        // >>> INTER-SERVICE REST CALL: GET 8082 /api/enrollments/exists -> parsed into EnrollmentStatusDTO
        EnrollmentStatusDTO response = restTemplate.getForObject(url, EnrollmentStatusDTO.class);
        return response != null && response.isEnrolled();
    }
}
