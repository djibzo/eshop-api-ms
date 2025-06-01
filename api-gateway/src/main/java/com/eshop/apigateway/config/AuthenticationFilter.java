package com.eshop.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {


    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getURI().getPath().contains("/api/auth")) {
            return chain.filter(exchange);
        }

        if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    System.out.println("verif token : "+token);
                    jwtService.validateToken(token); // valide
                    String email = jwtService.extractEmail(token);
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-Email", email)
                            .build();
                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    return chain.filter(mutatedExchange);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return onError(exchange, "Token JWT invalide : " + e.getMessage(), HttpStatus.UNAUTHORIZED);
                }
            } else {
                System.out.println("else 2");
                return onError(exchange, "Authorization header invalide", HttpStatus.UNAUTHORIZED);
            }
        } else {
            System.out.println("else 3");
            return onError(exchange, "Authorization header manquant", HttpStatus.UNAUTHORIZED);
        }
    }


    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}