package com.placement.reporting.auth;//package com.placement.reporting.auth;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class AuthFilter extends OncePerRequestFilter {
//
//    private final AuthValidationService authValidationService;
//
//    public AuthFilter(AuthValidationService authValidationService) {
//        this.authValidationService = authValidationService;
//    }
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//
//        // Only protect report APIs
//        if (path.startsWith("/api/reports")) {
//            String token = request.getHeader("X-SESSION-TOKEN");
//
//            try {
//                authValidationService.validatePlacementOfficer(token);
//            } catch (Exception e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json");
//                response.getWriter().write("{\"error\":\"Unauthorized\"}");
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
