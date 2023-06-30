package com.arv.api.filter;

import com.arv.api.config.RouteValidator;
import com.arv.api.utils.JwtTokenValidator;
import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AdminFilter extends AbstractGatewayFilterFactory<AdminFilter.Config> {

    @Autowired
    private RouteValidator routerValidator;
    @Autowired
    private JwtTokenValidator jwtUtil;

    public AdminFilter() {
        super(Config.class);
    }

    //    "ADMIN", "MANAGER"
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!routerValidator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    if (jwtUtil.isInvalid(authHeader) || jwtUtil.isNotHasRole("ADMIN", authHeader)) {
                        throw new Exception("invalid token");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}