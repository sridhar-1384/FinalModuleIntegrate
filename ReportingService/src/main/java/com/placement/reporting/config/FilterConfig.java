package com.placement.reporting.config;

import com.placement.reporting.auth.AuthFilter;
import com.placement.reporting.auth.AuthValidationService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(
            AuthValidationService authValidationService) {

        FilterRegistrationBean<AuthFilter> registrationBean =
                new FilterRegistrationBean<>();

        registrationBean.setFilter(
                new AuthFilter(authValidationService)
        );

        registrationBean.addUrlPatterns("/api/reports/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
