package com.arv.api.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public Predicate<ServerHttpRequest> isSecured = request -> true;

}
