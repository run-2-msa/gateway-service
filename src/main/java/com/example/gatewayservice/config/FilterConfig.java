package com.example.gatewayservice.config;

import com.example.gatewayservice.filter.CustomFilter;
import com.example.gatewayservice.filter.LoggingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// java로 필터 및 lb 작성

@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, CustomFilter customFilter, LoggingFilter loggingFilter){

        return builder.routes()
                .route(r ->
                        r.path("/first-service/**")
                        .filters( f -> f.addRequestHeader("first-request", "first-request-header")
                                        .addResponseHeader("first-response", "first-response-header")
                                        .filters(customFilter.apply(new CustomFilter.Config()),
                                                loggingFilter.apply(new LoggingFilter.Config())
                                                )
                                        // .rewritePath()
                        )
                        .uri("lb://FIRST-SERVICE")
                )
                .route(r ->
                        r.path("/second-service/**")
                        .filters( f -> f.addRequestHeader("second-request", "second-request-header")
                                        .addResponseHeader("second-response", "second-response-header")
                                        .filter(customFilter.apply(customFilter.newConfig()))
                        )
                        .uri("lb://SECOND-SERVICE")
                )
                .build();
    }
}
