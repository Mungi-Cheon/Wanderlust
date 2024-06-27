package com.travel.global.jwt;

import static com.travel.global.exception.type.ErrorType.TOKEN_AUTHORIZATION_FAIL;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.travel.global.exception.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtility {

    private final String issuer;
    private final Algorithm algorithm;
    private final long accessTokenValidityInMs;
    private final JWTVerifier verifier;

    public JwtTokenUtility(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.issuer}") String issuer,
        @Value("${jwt.access-token-expire-time}") long accessTokenValidityInMs) {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.issuer = issuer;
        this.accessTokenValidityInMs = accessTokenValidityInMs;
        this.verifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    public Long getAccessTokenUserId(final String accessToken) {
        // token 검사
        validAccessTokenWithThrow(accessToken);
        return extractUserId(accessToken);
    }


    private Long extractUserId(final String token) {
        DecodedJWT decodedJwt = verifier.verify(token);

        String tokeId = decodedJwt.getSubject();
        return Long.parseLong(tokeId);
    }

    // todo : 유효기간 만료된 경우 - 정훈님 하실부분
    private void validAccessTokenWithThrow(final String token) {
        try {
            DecodedJWT decodedJwt =verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthException(TOKEN_AUTHORIZATION_FAIL);
        }
    }
}
