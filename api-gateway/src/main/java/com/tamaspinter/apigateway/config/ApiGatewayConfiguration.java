package com.tamaspinter.apigateway.config;

import com.tamaspinter.apigateway.auth.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ApiGatewayConfiguration {

    final AuthenticationFilter filter;

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/user/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))

                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))

                .route("config-service", r -> r.path("/config/**", "/storage-provider/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://config-service"))

                .route("sharing-service", r -> r.path("/share/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://sharing-service"))

                .route("file-upload-service", r -> r.path("/upload/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://file-upload-service"))

                .route("file-retrieval-service", r -> r.path("/files/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://file-retrieval-service"))

                .build();
    }
}
