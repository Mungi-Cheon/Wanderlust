package com.travel.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.global.security.jwt.JwtAuthenticationToken;
import com.travel.global.util.cookie.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.log.LogMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();

    private final AuthenticationManager authenticationManager;

    private final AuthenticationDetailsSource<HttpServletRequest, ?>
        authenticationDetailsSource = new WebAuthenticationDetailsSource();



    private final Map<String, String> excludeUrls;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,

        Map<String, String> excludeUrls) {

        this.authenticationManager = authenticationManager;
        this.excludeUrls = excludeUrls;
    }

    private static final String grantType = "Bearer";

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
        String uri = request.getRequestURI();

        String method = request.getMethod();
        return excludeUrls.entrySet().stream()
            .anyMatch(entry -> uri.startsWith(entry.getKey()) && method.equals(entry.getValue()));

    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        String accessToken = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access-token".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        // 현재 보안 컨텍스트에 이미 인증 정보가 있는지 확인
        if (this.securityContextHolderStrategy.getContext().getAuthentication() != null) {
            this.logger.debug(LogMessage
                .of(() -> "SecurityContextHolder에 이미 JWT 토큰이 포함되어 있습니다: '"
                    + this.securityContextHolderStrategy.getContext().getAuthentication()
                    + "'"));
            chain.doFilter(request, response);
            return;
        }

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                this.logger.debug("Cookie Name: " + cookie.getName() + ","
                    + " Cookie Value: " + cookie.getValue());
            }
        }

        // 'refresh-token' 쿠키에서 리프레시 토큰 추출
        Optional<Cookie> refreshToken = CookieUtil.getCookie(request, "refresh-token");

        if (accessToken != null) {

            String refreshTokenValue = refreshToken
                .map(Cookie::getValue)
                .orElse(null);

            JwtAuthenticationToken jwtAuth = JwtAuthenticationToken
                .unauthenticated(accessToken, refreshTokenValue);

            setDetails(request, jwtAuth);

            try {
//                 JWT 인증 시도
                Authentication authResult = this
                    .authenticationManager
                    .authenticate(jwtAuth);

                SecurityContext context = this
                    .securityContextHolderStrategy
                    .createEmptyContext();

                context.setAuthentication(authResult);

                this.securityContextHolderStrategy.setContext(context);
                System.out.println("true");
            } catch (AuthenticationException ex) {
                System.out.println("false");
            }
        }
        // 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }

    protected void setDetails(HttpServletRequest request, JwtAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
