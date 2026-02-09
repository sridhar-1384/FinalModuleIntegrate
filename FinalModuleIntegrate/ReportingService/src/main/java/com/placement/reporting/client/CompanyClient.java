package com.placement.reporting.client;

import com.placement.reporting.dto.CompanyDto;
import com.placement.reporting.dto.JobDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CompanyClient {

    private final RestTemplate restTemplate;

    public CompanyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CompanyDto> getAllCompanies() {
        String url = "http://localhost:8080/api/companies/list";
        CompanyDto[] response = restTemplate.getForObject(url, CompanyDto[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }

    public List<JobDto> getAllJobs() {
        String url = "http://localhost:8080/api/jobs/list";
        JobDto[] response = restTemplate.getForObject(url, JobDto[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }
}

