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
    private final long refreshTokenValidityInMs;

    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,

        @Value("${jwt.issuer}") String issuer,

        @Value("${jwt.access-token-expire-time}") long accessTokenValidityInMs,

//        RefreshTokenService refreshTokenService,

        @Value("${jwt.refresh-token-expire-time}") long refreshTokenValidityInMs) {

        this.algorithm = Algorithm.HMAC256(secretKey);
        this.issuer = issuer;
        this.accessTokenValidityInMs = accessTokenValidityInMs;
        this.refreshTokenValidityInMs = refreshTokenValidityInMs;
    }

    public String generateAccessToken(Long id) {
        LocalDateTime now = LocalDateTime.now();
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(String.valueOf(id))
            .withIssuedAt(now.atZone(ZoneId.systemDefault()).toInstant())
            .withExpiresAt(
                now.plus(accessTokenValidityInMs, ChronoUnit.MILLIS)
                    .atZone(ZoneId.systemDefault())
                    .toInstant())
            .sign(algorithm);
    }

//
//    public String generateRefreshToken(Long id) {
//        LocalDateTime now = LocalDateTime.now();
//        return JWT.create()
//            .withIssuer(issuer)
//            .withSubject(String.valueOf(id))
//            .withIssuedAt(now.atZone(ZoneId.systemDefault()).toInstant())
//            .withExpiresAt(
//                now.plus(refreshTokenValidityInMs, ChronoUnit.MILLIS)
//                    .atZone(ZoneId.systemDefault())
//                    .toInstant())
//            .sign(algorithm);
//    }

//    public String refreshAccessToken(Long userId) {
//        try {
//            DecodedJWT decodedJWT = verifier.verify(String.valueOf(userId));
//            String tokenId = decodedJWT.getSubject();
//            return generateAccessToken(Long.valueOf(tokenId));
//        } catch (TokenExpiredException ex) {
//            // access ->
//            // <- 액세트 토큰 만료
//            // ---------------------------------------------
//            // reissue (access update) >->
//            // <-  재로그인 하라고 요청 HttpStatus.UNAUTHORIZED
//            // 로그인페이지갈게
//            throw new TokenExpiredException(ex.getMessage(), ex.getExpiredOn());
//        } catch (JWTVerificationException e) {
//            throw new InvalidJwtException("Invalid refresh token", e);
//        }
//    }
}
