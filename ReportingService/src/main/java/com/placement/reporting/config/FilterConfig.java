package com.placement.reporting.config;

import com.placement.reporting.auth.AuthFilter;
import com.placement.reporting.auth.AuthValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(AuthValidationService authValidationService) {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(authValidationService)); // ✅ pass it here
        registrationBean.addUrlPatterns("/api/reports/*");
        return registrationBean;
    }
}