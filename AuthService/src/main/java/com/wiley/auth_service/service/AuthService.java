package com.wiley.auth_service.service;

import com.wiley.auth_service.dto.*;
import com.wiley.auth_service.model.Role;
import com.wiley.auth_service.model.Sessions;
import com.wiley.auth_service.model.Users;
import com.wiley.auth_service.repository.SessionRepository;
import com.wiley.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    //------------- sridhar --------------------------

    public String login(LoginRequest request){
        Users user=userRepository.findByEmail(request.getEmail());

        if(user==null)
            throw new IllegalArgumentException("User does not exist");


        if(!encoder.matches(request.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid password");

        Sessions session=new Sessions();
        String token= UUID.randomUUID().toString();
        session.setSessionToken(token);
        session.setUserId(user.getId());
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        sessionRepository.save(session);

        return token;
    }

    public boolean logout(String token) {
       Sessions session=sessionRepository.findBySessionToken(token);
        if(session!=null){
            session.setExpiresAt(LocalDateTime.now());
            return true;
        }
        return false;
    }

    // ---------------------- nitisha ---------------------------------------

    public RegisterStudentResponse registerStudent(RegisterStudentRequest req) {

        // CHECK: email already exists
        if (userRepository.findByEmail(req.getEmail()) != null) {
            return new RegisterStudentResponse("error", "Email already exists!");
        }

        // Save login data
        Users user = new Users();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.STUDENT);

        userRepository.save(user);

        // ------------------------------------------------------------
        // Call Team-2's POST API here to save student academic details
        // -------------------------------------------------------------

        return new RegisterStudentResponse("success", "Student registered successfully!");
    }

    // --------------------------- abhishek -------------------------------------------

    public void registerCompany(CompanyRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.COMPANY_HR);
        user.setIsActive(true);

        userRepository.save(user);
    }

    // ------------------------ riya --------------------------------------------
    // Used by /validate API
    public ResponseEntity<?> validateSession(String token) {

        Sessions session = sessionRepository.findBySessionToken(token);

        if (session == null) {
            return ResponseEntity.status(401)
                    .body("Invalid session");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            sessionRepository.delete(session);
            return ResponseEntity.status(401)
                    .body("Session expired");
        }

        Users user = userRepository
                .findById(session.getUserId())
                .orElseThrow();

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("isActive", user.getIsActive());

        return ResponseEntity.ok(response);
    }

    // Helper method for change-password
    public Sessions getValidSession(String token) {

        Sessions session = sessionRepository.findBySessionToken(token);

        if (session == null ||
                session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Session invalid or expired");
        }

        return session;
    }

    public ResponseEntity<?> changePassword(String token, ChangePasswordRequest request){
        Sessions session = getValidSession(token);

        Users user = userRepository.findById(session.getUserId())
                .orElseThrow();

        System.out.println("RAW password from request = " + request.getCurrentPassword());
        System.out.println("HASH from DB = " + user.getPassword());
        System.out.println("BCrypt match result = " +
                encoder.matches(request.getCurrentPassword(), user.getPassword()));

        if (!encoder.matches(
                request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("Current password incorrect");
        }

        user.setPassword(
                encoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }
}
