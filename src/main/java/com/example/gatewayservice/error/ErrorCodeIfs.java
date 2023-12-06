package com.example.gatewayservice.error;

// common service로 옮길 예정
public interface ErrorCodeIfs {
    Integer getHttpStatusCode();
    Integer getErrorCode();
    String getMessage();
}
