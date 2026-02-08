package com.placement.reporting.service;

import org.springframework.stereotype.Service;

@Service
public class AuthValidationService {

    private static final boolean AUTH_ENABLED = false;

    public void validatePlacementOfficer(String sessionToken) {

        if (!AUTH_ENABLED) {
            return;
        }

        if (sessionToken == null || sessionToken.isBlank()) {
            throw new RuntimeException("Missing session token");
        }

        throw new RuntimeException("Access denied");
    }
}





//package com.placement.reporting.service;
//
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class AuthValidationService {
//
//    // Change here ONLY if Auth Service URL or header changes
//    private static final String AUTH_CURRENT_USER_URL =
//            "http://localhost:8081/api/auth/current-user";
//
//    private static final String SESSION_HEADER = "X-SESSION-TOKEN";
//    private static final String REQUIRED_ROLE = "PLACEMENT_OFFICER";
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public void validatePlacementOfficer(String sessionToken) {
//
//        if (sessionToken == null || sessionToken.isBlank()) {
//            throw new RuntimeException("Missing session token");
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(SESSION_HEADER, sessionToken);
//
//        HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<AuthUserResponse> response =
//                restTemplate.exchange(
//                        AUTH_CURRENT_USER_URL,
//                        HttpMethod.GET,
//                        entity,
//                        AuthUserResponse.class
//                );
//
//        if (response.getBody() == null ||
//                !REQUIRED_ROLE.equals(response.getBody().getRole())) {
//            throw new RuntimeException("Access denied");
//        }
//    }
//
//    // Minimal response model (no entity)
//    private static class AuthUserResponse {
//        private String role;
//
//        public String getRole() {
//            return role;
//        }
//        public void setRole(String role) {
//            this.role = role;
//        }
//    }
//}
