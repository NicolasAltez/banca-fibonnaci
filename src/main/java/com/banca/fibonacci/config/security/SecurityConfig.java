package com.banca.fibonacci.config.security;

import com.banca.fibonacci.config.security.filters.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Value("${api.security.key}")
    private String apiKey;

    @Value("${api.security.name.header}")
    private String apiHeaderName;


    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilter() {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiKeyFilter(apiKey, apiHeaderName));
        registrationBean.addUrlPatterns("/api/fibonacci/*");
        return registrationBean;
    }
}

