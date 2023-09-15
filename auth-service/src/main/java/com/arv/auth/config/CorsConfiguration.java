package com.arv.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigurer() {

        String[] hosts = {
                "http://35.244.33.213:8090",
                "http://35.244.33.213:8081",
                "http://35.244.33.213:8082",
                "http://35.244.33.213:8000",
                "http://85.31.233.21:8090",
                "http://85.31.233.21:8081",
                "http://85.31.233.21:8082",
                "http://85.31.233.21:8000",
                "http://localhost:8082",
        };
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(hosts)
                        .allowedMethods("POST", "PUT", "PATCH", "GET", "DELETE", "OPTIONS")
                        .allowedHeaders("Origin", "X-Api-Key", "X-Requested-With", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Headers")
                        .exposedHeaders("Authorization")
                        .allowCredentials(true);
            }
        };
    }
}