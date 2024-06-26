package com.travel.global.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.travel.global.security.exception.InvalidJwtException;
import com.travel.global.security.jwt.JwtAuthenticationToken;
import com.travel.global.security.service.RefreshTokenService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider implements AuthenticationProvider {

    private static final String SECRET_KEY = "0123456789";
    private final String issuer;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final RefreshTokenService refreshTokenService;
    private final long refreshTokenValidityInMs;
    private long accessTokenValidityInMs;

    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,

        @Value("${jwt.issuer}") String issuer,

        @Value("${jwt.access-token-expire-time}") long accessTokenValidityInMs,

        RefreshTokenService refreshTokenService,

        @Value("${jwt.refresh-token-expire-time}") long refreshTokenValidityInMs) {

        this.algorithm = Algorithm.HMAC256(secretKey);
        this.issuer = issuer;
        this.accessTokenValidityInMs = accessTokenValidityInMs;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenValidityInMs = refreshTokenValidityInMs;
        this.verifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String accessTokenValue = (String) authentication.getPrincipal();

        JwtAuthenticationToken authResult;

        try {
            // accessToken 검증
            DecodedJWT decodedJwt = verifier.verify(accessTokenValue);
            String email = decodedJwt.getSubject();

            List<SimpleGrantedAuthority> authorities = decodedJwt
                .getClaim("authgr")
                .asList(SimpleGrantedAuthority.class);

            authResult = JwtAuthenticationToken.authenticated(email, authorities);
        } catch (TokenExpiredException ex) {
            // accessToken이 만료된 경우
            throw new TokenExpiredException(ex.getMessage(), ex.getExpiredOn());
        } catch (JWTVerificationException ex) {
            throw new InvalidJwtException(ex.getMessage(), ex);
        }

        authResult.setDetails(authentication.getDetails());
        return authResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
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
