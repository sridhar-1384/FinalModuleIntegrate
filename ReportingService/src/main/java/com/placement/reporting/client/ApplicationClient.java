package com.placement.reporting.client;

import com.placement.reporting.dto.ApplicationDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationClient {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public ApplicationClient(RestTemplate restTemplate, WebClient webClient) {
        this.restTemplate = restTemplate;
        this.webClient=webClient;
    }

//    public List<ApplicationDto> getApplicationsByJob(Long jobId) {
//        String url = "http://localhost:8084/api/applications/job/" + jobId;
//        ApplicationDto[] response = restTemplate.getForObject(url, ApplicationDto[].class);
//        return response != null ? Arrays.asList(response) : List.of();
//    }

    public List<ApplicationDto> getApplicationsByJob(Long jobId, String sessionToken) {

        String url = "http://localhost:8084/api/applications/job/" + jobId;

        return webClient.get()
                .uri(url)
                .header("SESSION-TOKEN", sessionToken)
                .retrieve()
                .bodyToFlux(ApplicationDto.class)
                .collectList()
                .block();   // block because your method returns List, not Mono/List
    }


    public List<ApplicationDto> getApplicationsByStudent(Long studentId) {
//        String url = "http://localhost:8084/api/applications/student/" + studentId;
//        ApplicationDto[] response = restTemplate.getForObject(url, ApplicationDto[].class);
//        return response != null ? Arrays.asList(response) : List.of();

        List<ApplicationDto> list=new ArrayList<ApplicationDto>();
        list.add(new ApplicationDto(1L,1L,1L,"ACTIVE"));
        list.add(new ApplicationDto(2L,2L,1L,"ACTIVE"));
        list.add(new ApplicationDto(3L,1L,2L,"ACTIVE"));

        return list;

    }
}

