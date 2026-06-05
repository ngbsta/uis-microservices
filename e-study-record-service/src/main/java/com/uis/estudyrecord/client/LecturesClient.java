package com.uis.estudyrecord.client;

import com.uis.estudyrecord.dto.CourseDTO;
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
 * Inter-service communication: e-study-record asks lectures-service for the
 * course catalogue so a student's grades can show the real class name instead
 * of a bare course id. Uses Spring {@link RestTemplate}; responses are DTOs.
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

    public List<CourseDTO> getCourses() {
        try {
            List<CourseDTO> courses = restTemplate.exchange(
                    baseUrl + "/api/courses",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CourseDTO>>() {}
            ).getBody();
            return courses == null ? Collections.emptyList() : courses;
        } catch (Exception e) {
            // lectures-service may be down — degrade gracefully (no names)
            return Collections.emptyList();
        }
    }

    /** Convenience: courseId -> course name map. */
    public Map<Long, String> courseNames() {
        Map<Long, String> map = new LinkedHashMap<>();
        for (CourseDTO c : getCourses()) {
            map.put(c.getId(), c.getName());
        }
        return map;
    }

    /** Convenience: courseId -> fixed course credits map (credits are owned by the course). */
    public Map<Long, Integer> courseCredits() {
        Map<Long, Integer> map = new LinkedHashMap<>();
        for (CourseDTO c : getCourses()) {
            map.put(c.getId(), c.getCredits());
        }
        return map;
    }

    /**
     * Inter-service: the student's mid-term test score for a course, taken from the
     * My Lectures Sheet service (lectures, 8083). This is what links the lectures
     * mid-term to the overall grade computed here. Returns 0 if none / unreachable.
     */
    public double midtermScore(Long studentId, Long courseId) {
        try {
            List<Map<String, Object>> results = restTemplate.exchange(
                    baseUrl + "/api/test-results?studentId=" + studentId + "&courseId=" + courseId,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();
            if (results != null && !results.isEmpty()) {
                Object score = results.get(0).get("score");
                if (score instanceof Number n) return n.doubleValue();
            }
        } catch (Exception e) {
            // lectures down — degrade to 0
        }
        return 0.0;
    }
}
