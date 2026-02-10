package com.placement.reporting.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class StudentClient {

    private final RestTemplate restTemplate;

    public StudentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllStudents() {
        String url = "http://localhost:8082/api/admin/students";

        Object[] response = restTemplate.getForObject(url, Object[].class);

        if (response == null) {
            return List.of();
        }

        // Cast each element to Map<String, Object>
        return Arrays.stream(response)
                .map(obj -> (Map<String, Object>) obj)
                .toList();
    }
}
