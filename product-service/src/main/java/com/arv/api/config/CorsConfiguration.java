package com.arv.api.config;

//@Configuration
//@SuppressWarnings("unused")
//public class CorsConfiguration extends
//        org.springframework.web.cors.CorsConfiguration {
//
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//
//        final CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
//        corsConfig.setMaxAge(3600L);
//        corsConfig.setAllowedMethods(Arrays.asList("*", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        corsConfig.addAllowedHeader("*");
//
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }
//}


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        String[] hosts = {
//                "https://35.244.31.172:8090",
//                "https://35.244.31.172:8081",
//                "https://35.244.31.172:8082",
//                "http://35.244.31.172:8090",
//                "http://35.244.31.172:8081",
//                "http://35.244.31.172:8082",
//                "https://e-store-0.web.app",
//                "https://localhost:8090",
//                "https://localhost:8081",
//                "https://localhost:8082",
//                "https://localhost:*/**"
//        };
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(false);
//    }

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