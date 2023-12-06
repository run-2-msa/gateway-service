package com.example.gatewayservice.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// common service로 옮길 예정
@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCodeIfs{
    OK(HttpStatus.OK.value(), 2000, "유효 토큰"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), 2001, "유효 하지 않은 토큰"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), 2002, "만료된 토큰"),
    TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), 2003, "유효 하지 않은 토큰 - 예외"),
    AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), 2004, "인증 헤더 토큰 없음"),
    SUBJECT_EMPTY(HttpStatus.UNAUTHORIZED.value(), 2005, "SUBJECT is empty"),
    ;
    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
