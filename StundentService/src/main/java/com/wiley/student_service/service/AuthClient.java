package com.wiley.student_service.service;

import com.wiley.student_service.dto.AuthUserDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class AuthClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String AUTH_URL = "http://localhost:8081/api/auth/validate";
    //  auth-service URL

    public AuthUserDto validateSession(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<AuthUserDto> response =
                restTemplate.exchange(
                        AUTH_URL,
                        HttpMethod.GET,
                        entity,
                        AuthUserDto.class
                );

        return response.getBody();
    }
}

