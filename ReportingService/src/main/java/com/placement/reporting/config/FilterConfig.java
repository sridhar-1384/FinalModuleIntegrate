package com.placement.reporting.config;//package com.placement.reporting.config;
//
//import com.placement.reporting.auth.AuthFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<AuthFilter> authFilter(AuthFilter authFilter) {
//        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(authFilter);
//        registrationBean.addUrlPatterns("/api/reports/*");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//}
