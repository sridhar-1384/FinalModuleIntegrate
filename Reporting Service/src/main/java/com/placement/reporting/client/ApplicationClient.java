package com.placement.reporting.client;

import com.placement.reporting.dto.ApplicationDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationClient {

    private final RestTemplate restTemplate;

    public ApplicationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ApplicationDto> getApplicationsByJob(Long jobId) {
        String url = "http://localhost:8080/api/applications/job/" + jobId;
        ApplicationDto[] response = restTemplate.getForObject(url, ApplicationDto[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }

    public List<ApplicationDto> getApplicationsByStudent(Long studentId) {
        String url = "http://localhost:8080/api/applications/student/" + studentId;
        ApplicationDto[] response = restTemplate.getForObject(url, ApplicationDto[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }
}

