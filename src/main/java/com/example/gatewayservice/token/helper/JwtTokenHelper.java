package com.example.gatewayservice.token.helper;

import com.example.gatewayservice.common.utils.StringUtils;
import com.example.gatewayservice.error.ErrorCodeIfs;
import com.example.gatewayservice.error.TokenErrorCode;
import com.example.gatewayservice.token.ifs.TokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Primary
public class JwtTokenHelper implements TokenHelper {
    @Value("${token.secret.key}")
    private String secretKey;

    @Override
    public ErrorCodeIfs validationToken(String token) {

        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try {

            var result = parser.parseClaimsJws(token);

            var subject = result.getBody().getSubject();

            if(StringUtils.isNotNullOrNotBlank(subject)){
                return TokenErrorCode.SUBJECT_EMPTY;
            }

            return TokenErrorCode.OK;
            // return new HashMap<String, Object>(result.getBody());

        }catch (Exception e){
            if(e instanceof SignatureException){
                // 토큰이 유효하지 않을 때
                return TokenErrorCode.INVALID_TOKEN;
            } else if (e instanceof ExpiredJwtException) {
                // 만료된 토큰
                return TokenErrorCode.EXPIRED_TOKEN;
            }else {
                // 그 외 예외
                return TokenErrorCode.TOKEN_EXCEPTION;
            }
        }
    }

}
