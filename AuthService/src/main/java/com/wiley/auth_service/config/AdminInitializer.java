package com.wiley.auth_service.config;

import com.wiley.auth_service.model.Role;
import com.wiley.auth_service.model.Users;
import com.wiley.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {

            String adminEmail = "admin@gmail.com";

            Users adminOptional = userRepository.findByEmail(adminEmail);

            if (adminOptional==null) {

                Users admin = new Users();
                admin.setName("Admin");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.PLACEMENT_OFFICER);
                admin.setIsActive(true);

                userRepository.save(admin);

                System.out.println("✅ Admin user created");
            } else {
                System.out.println("ℹ️ Admin already exists");
            }
        };
    }
}

