package com.placement.reporting.auth;

public class AuthUserResponse {

    private String role;

    public AuthUserResponse() {
    }

    public AuthUserResponse(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
