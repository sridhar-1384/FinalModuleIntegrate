package com.placement.reporting.auth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthValidationService {

    // Auth Service endpoint
    private static final String AUTH_CURRENT_USER_URL =
            "http://localhost:8081/api/auth/current-user";

    private static final String SESSION_HEADER = "X-SESSION-TOKEN";
    private static final String REQUIRED_ROLE = "PLACEMENT_OFFICER";

    private final RestTemplate restTemplate = new RestTemplate();

    public void validatePlacementOfficer(String sessionToken) {

        if (sessionToken == null || sessionToken.trim().isEmpty()) {
            throw new RuntimeException("Session token missing");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(SESSION_HEADER, sessionToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<AuthUserResponse> response;

        try {
            response = restTemplate.exchange(
                    AUTH_CURRENT_USER_URL,
                    HttpMethod.GET,
                    requestEntity,
                    AuthUserResponse.class
            );
        } catch (Exception ex) {
            throw new RuntimeException("Invalid session");
        }

        AuthUserResponse user = response.getBody();

        if (user == null || user.getRole() == null) {
            throw new RuntimeException("Invalid auth response");
        }

        if (!REQUIRED_ROLE.equals(user.getRole())) {
            throw new RuntimeException("Access denied");
        }
    }
}
