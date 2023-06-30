package com.arv.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    @RequestMapping("/productFallBack")
    public Mono<String> orderServiceFallback() {
        return Mono.just("Service under maintenance!");
    }
    @RequestMapping("/authFallBack")
    public Mono<String> authServiceFallback() {
        return Mono.just("Service is down contact admin!");
    }
}
