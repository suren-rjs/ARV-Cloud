package com.arv.api.config;

import io.micrometer.core.lang.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate template(){
       return new RestTemplate();
    }
    @Bean
    public WebMvcConfigurer configure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                return new WebMvcConfigurer() {
                    @Override
                    public void addCorsMappings(@lombok.NonNull CorsRegistry registry) {
                        registry.addMapping("/**")
//                        .allowedOrigins("https://e-store-0.web.app")
                                .allowedOrigins("*")
                                .allowedMethods("GET", "POST", "PUT", "DELETE")
                                .allowedHeaders("Content-Type", "Authorization")
                                .exposedHeaders("Custom-Header1", "Custom-Header2")
                                .allowCredentials(true)
                                .maxAge(3600);
                    }
                };
            }
        };
    }

}
