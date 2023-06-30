package com.arv.api.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private final List<String> OPEN_API_END_POINTS = Arrays.asList("/auth-help", "/auth/**", "/v3/**", "/help/**", "/swagger-ui/**", "/notification/**", "/", "/public/**", "/eureka");

    public Predicate<ServerHttpRequest> isSecured =
            request -> OPEN_API_END_POINTS
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
