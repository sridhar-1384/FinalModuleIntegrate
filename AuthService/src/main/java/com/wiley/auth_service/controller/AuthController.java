package com.wiley.auth_service.controller;

import com.wiley.auth_service.dto.*;
import com.wiley.auth_service.model.Sessions;
import com.wiley.auth_service.model.Users;
import com.wiley.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    // -------------------- nitisha ---------------------------------------

    @PostMapping("/register-student")
    public RegisterStudentResponse registerStudent(@Valid @RequestBody RegisterStudentRequest req) {
        return authService.registerStudent(req);
    }

    // --------------------- sridhar ----------------------------------------

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        Map<String,String> res=new HashMap<>();
        String token= authService.login(request);
        res.put("sessionToken",token);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String sessionToken){
        boolean success= authService.logout(sessionToken);
        if(success)
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }

    // -------------- abhishek ------------------------------------
    @PostMapping("/register/company")
    public ResponseEntity<String> registerCompany(@Valid @RequestBody CompanyRegisterRequest request) {

        authService.registerCompany(request);
        return ResponseEntity.ok("Company HR registered successfully");
    }

    // ---------------- riya --------------------------------------

    @GetMapping("/validate")
    public ResponseEntity<?> validateSession(
            @RequestHeader("SESSION-TOKEN") String token) {

        return authService.validateSession(token);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("SESSION-TOKEN") String token,
            @RequestBody ChangePasswordRequest request) {

        return authService.changePassword(token,request);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader("SESSION-TOKEN") String token) {

        return authService.validateSession(token);

    }

}
