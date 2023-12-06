package com.example.gatewayservice.token.ifs;

import com.example.gatewayservice.error.ErrorCodeIfs;

public interface TokenHelper {
    ErrorCodeIfs validationToken(String token);
}
