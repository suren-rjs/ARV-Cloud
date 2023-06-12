package com.arv.cloudgateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SuppressWarnings("All")
@RestController
public class FallbackController {
    @RequestMapping("/productFallBack")
    public Mono<String> orderServiceFallback() {
        return Mono.just("Access not available, try after some time!");
    }

    @RequestMapping("/userFallBack")
    public Mono<String> userServiceFallback() {
        return Mono.just("Service down, try again later!");
    }
}
