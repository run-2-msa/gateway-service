package com.example.gatewayservice.filter;

import com.example.gatewayservice.error.ErrorCodeIfs;
import com.example.gatewayservice.error.TokenErrorCode;
import com.example.gatewayservice.token.helper.JwtTokenHelper;
import com.example.gatewayservice.token.ifs.TokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final TokenHelper tokenHelper;

    public AuthorizationHeaderFilter(TokenHelper tokenHelper) {
        super(Config.class);
        this.tokenHelper = tokenHelper;
    }

    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        // Custom Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            // var tokenHelper = config.getTokenHelper();

            // WEB - chrome의 경우 요청 하기 전에 OPTION 이라는 API를 요청 해서 해당 메소드를 지원 하는지 체크 하는 API가 있음 (pre flight)
            // 그래서 이것을 통과 시켜 줄것임
            /*if(HttpMethod.OPTIONS.matches(request.getMethod())){
                return true;
            }*/

            // js, html, png 같은 리소스를 요청하는 경우 통과
            /*if(handler instanceof ResourceHttpRequestHandler){
                return true;
            }*/
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "인증 헤더 토큰 없음", HttpStatus.UNAUTHORIZED);
            }

            var authorizationHeader =
                    Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

            var jwt = authorizationHeader.replace("Bearer", "");


            // token validation return
            var jwtValid = tokenHelper.validationToken(jwt);

            if(!jwtValid.equals(TokenErrorCode.OK)){
                return onError(exchange, jwtValid.getMessage(), HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    // WebFlux 방식 비동기 처리 반환 단위 단일 값 Mono, 단일 값 아닌 것 Flux
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse(); // 비동기 방식 이고 response에서 servlet 방식 사용 하지 않음

        response.setStatusCode(httpStatus);
        log.error(message);

        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
        /*


        // TODO 1. SPA 테스트 ( pre-flight, gateway cors, service cors )
        // TODO 2. YAML route 설정 java 코드로 변경 하기
        // TODO 3.응답 java 공통 spec 으로 응답 주기 (2번이 선행 되어야 함)
        // TODO 4. WebFlux 스터디 (반드 시는 아니지만.. 이걸 알아야 3번이나 필터 관련 해서 이해가 원활 할듯) 키워드는 비동기
        https://cloud.spring.io/spring-cloud-gateway/reference/html/#configuring-route-predicate-factories-and-gateway-filter-factories
        https://stackoverflow.com/questions/62520341/spring-cloud-gateway-modifyresponsebody-of-flux

        */
//        return response.setComplete();
    }

    public static class Config {
    }
}
