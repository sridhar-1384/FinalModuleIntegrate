package com.wiley.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter
@Setter
@Data
public class CompanyRegisterRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
     private String name;
    private String hrName;
}
