package com.databi.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/v1/driver/**")
                        .filters(f -> f
                                .prefixPath("/api")
                        ).uri("http://localhost:8080/driver")
                )
                .route(r -> r.path("/v1/order/**")
                        .filters(f -> f
                                .prefixPath("/api")
                        ).uri("http://localhost:8081/order")
                )
                .build();
    }
}