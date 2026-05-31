package com.uis.examregistration.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Inter-service communication: asks the e-study-record-service whether a student
 * is enrolled in a course before allowing an exam registration.
 * (Week 10 requirement: "each app is communicating with each other".)
 */
@Component
public class EnrollmentClient {

    private final RestClient restClient;

    public EnrollmentClient(@Value("${estudyrecord.base-url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public boolean isEnrolled(Long studentId, Long courseId) {
        Map<?, ?> response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/enrollments/exists")
                        .queryParam("studentId", studentId)
                        .queryParam("courseId", courseId)
                        .build())
                .retrieve()
                .body(Map.class);
        return response != null && Boolean.TRUE.equals(response.get("enrolled"));
    }
}
