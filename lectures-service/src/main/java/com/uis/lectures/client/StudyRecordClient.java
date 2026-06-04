package com.uis.lectures.client;

import com.uis.lectures.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Inter-service communication: lectures-service asks e-study-record-service for the
 * student list, so the UI's student selectors come from the backend (single source of
 * truth) instead of a hardcoded frontend list. Uses Spring {@link RestTemplate}.
 */
@Component
public class StudyRecordClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public StudyRecordClient(RestTemplate restTemplate,
                             @Value("${estudyrecord.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<StudentDTO> getStudents() {
        try {
            List<StudentDTO> students = restTemplate.exchange(
                    baseUrl + "/api/students",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<StudentDTO>>() {}
            ).getBody();
            return students == null ? Collections.emptyList() : students;
        } catch (Exception e) {
            return Collections.emptyList();   // degrade gracefully
        }
    }
}
