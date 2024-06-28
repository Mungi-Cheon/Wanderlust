package com.travel.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 말그대로 토큰을 발행해준다.
@Component
public class JwtProvider {

    private final String issuer;
    private final Algorithm algorithm;
    private final long accessTokenValidityInMs;

    public JwtProvider(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.issuer}") String issuer,
        @Value("${jwt.access-token-expire-time}") long accessTokenValidityInMs
    ) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.issuer = issuer;
        this.accessTokenValidityInMs = accessTokenValidityInMs;
    }

    public String generateAccessToken(Long id) {
        LocalDateTime now = LocalDateTime.now();
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(String.valueOf(id))
            .withIssuedAt(now.atZone(ZoneId.systemDefault()).toInstant())
            .withExpiresAt(
                now.plus(accessTokenValidityInMs, ChronoUnit.SECONDS)
                    .atZone(ZoneId.systemDefault())
                    .toInstant())
            .sign(algorithm);
    }
}
