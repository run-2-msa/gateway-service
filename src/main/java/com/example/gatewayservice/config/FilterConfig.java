package com.example.gatewayservice.config;

import com.example.gatewayservice.filter.CustomFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// java로 커스텀 필터 등록 방법 yaml이랑 택 1
/*

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final CustomFilter customFilter; // java로 커스텀 필터 등록 방법
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r ->
                        r.path("/first-service/**")
                        .filters( f -> f.addRequestHeader("first-request", "first-request-header")
                                        .addResponseHeader("first-response", "first-response-header")
                                .filter(customFilter.apply(customFilter.newConfig())) // java로 커스텀 필터 등록 방법
                        )
                        .uri("http://localhost:8081")
                )
                .route(r ->
                        r.path("/second-service/**")
                        .filters( f -> f.addRequestHeader("second-request", "second-request-header")
                                        .addResponseHeader("second-response", "second-response-header"))
                        .uri("http://localhost:8082")
                )
                .build();
    }
}
*/
