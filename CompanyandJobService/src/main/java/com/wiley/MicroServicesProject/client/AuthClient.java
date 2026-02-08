package com.wiley.MicroServicesProject.client;

import com.wiley.MicroServicesProject.DTO.AuthUser;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public AuthUser validate(String sessionToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", sessionToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<AuthUser> res = restTemplate.exchange(
                "http://localhost:8081/api/auth/validate",
                HttpMethod.GET,
                entity,
                AuthUser.class
        );
        return res.getBody();
    }
}
