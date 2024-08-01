package com.travel.global.security.filter;

import static com.travel.global.security.type.TokenType.ACCESS;
import static com.travel.global.security.type.TokenType.MOCK;

import com.travel.global.exception.AuthException;
import com.travel.global.exception.type.ErrorType;
import com.travel.global.util.CookieUtil;
import com.travel.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final List<String> equalUrls;
    private final List<String> startsWithUrls;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return equalUrls.stream().anyMatch(path::equalsIgnoreCase) || startsWithUrls.stream()
            .anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().matches(".*\\.(js|html|png|ico|css)$") ||
            request instanceof ResourceHttpRequestHandler) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = cookieUtil.getTokenFromCookies(request.getCookies(),
            ACCESS.getName());

        if (accessToken == null) {
            String mockToken = cookieUtil.getTokenFromCookies(request.getCookies(),
                MOCK.getName());
            if (mockToken == null) {
                throw new AuthException(ErrorType.TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND);
            }
            setRequestContext(Long.valueOf(mockToken));
            return;
        }

        long memberId = jwtUtil.getAccessTokenMemberId(accessToken);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(memberId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void setRequestContext(Long memberId) {
        RequestAttributes requestContext = Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes());
        requestContext.setAttribute("memberId", memberId, RequestAttributes.SCOPE_REQUEST);
    }
}