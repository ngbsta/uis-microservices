package com.uis.examregistration.client;

import com.uis.examregistration.dto.CourseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Inter-service communication: exam-registration asks lectures-service for the course
 * catalogue so a sitting can carry the real course NAME. The name lives in exactly one
 * place (lectures-service); we never hardcode it in the UI.
 */
@Component
public class LecturesClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public LecturesClient(RestTemplate restTemplate,
                          @Value("${lectures.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /** courseId -> course name map (empty if lectures-service is unreachable). */
    public Map<Long, String> courseNames() {
        Map<Long, String> map = new LinkedHashMap<>();
        try {
            // >>> INTER-SERVICE REST CALL: GET 8083 /api/courses -> List<CourseDTO>
            List<CourseDTO> courses = restTemplate.exchange(
                    baseUrl + "/api/courses",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CourseDTO>>() {}
            ).getBody();
            if (courses != null) {
                for (CourseDTO c : courses) map.put(c.getId(), c.getName());
            }
        } catch (Exception e) {
            return Collections.emptyMap();   // degrade gracefully
        }
        return map;
    }
}
