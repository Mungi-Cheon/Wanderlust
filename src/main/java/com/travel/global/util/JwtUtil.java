package com.travel.global.util;

import static com.travel.global.exception.type.ErrorType.TOKEN_AUTHORIZATION_FAIL;
import static com.travel.global.exception.type.ErrorType.TOKEN_EXPIRED;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.travel.global.exception.AuthException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Algorithm algorithm;
    private final String issuer;
    private final JWTVerifier verifier;

    public JwtUtil(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.issuer}") String issuer) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.issuer = issuer;
        this.verifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    public String generateAccessToken(Long memberId) {
        return JWT.create()
            .withSubject(memberId.toString())
            .withIssuer(issuer)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
            .sign(algorithm);
    }

    public String generateRefreshToken(Long memberId) {
        return JWT.create()
            .withSubject(memberId.toString())
            .withIssuer(issuer)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 604800000L)) // 7 days expiration
            .sign(algorithm);
    }

    public Long getAccessTokenMemberId(final String accessToken) {
        // token 검사
        validAccessTokenWithThrow(accessToken);
        return extractMemberId(accessToken);
    }

    private Long extractMemberId(final String token) {
        DecodedJWT decodedJwt = verifier.verify(token);

        String tokenId = decodedJwt.getSubject();
        return Long.parseLong(tokenId);
    }

    private void validAccessTokenWithThrow(final String token) {
        try {
            verifyToken(token);
        } catch (TokenExpiredException e) {
            throw new AuthException(TOKEN_EXPIRED);
        } catch (JWTVerificationException e) {
            throw new AuthException(TOKEN_AUTHORIZATION_FAIL);
        }
    }

    private void verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }
}