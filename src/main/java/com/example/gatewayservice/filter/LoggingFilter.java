package com.example.gatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Logging Filter baseMessage: {}", config.getBaseMessage());

            if (config.isPreLogger()){
                log.info("Logging Filter Start: request id -> {}", request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // Mono: Webflux 비동기 서버 지원 단일 값 전달 객체
                if (config.isPostLogger()){
                    log.info("Logging Filter End: response status code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE); // 필터 우선 순위 최우선 지정
        // Ordered.LOWEST_PRECEDENCE 1, 2, 3, 4
    }

    @Data
    public static class Config {
        //Put the configuration properties for your filter here
        private String baseMessage = "기본 메시지 지정";
        private boolean preLogger = true;
        private boolean postLogger = true;
    }
}
