package com.travel.global.interceptor;

import com.travel.global.exception.AuthException;
import com.travel.global.exception.type.ErrorType;
import com.travel.global.jwt.JwtTokenUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtTokenUtility jwtTokenUtility;

    @Override
    public boolean preHandle(
        HttpServletRequest request,

        HttpServletResponse response,
        Object handler) throws Exception {
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // WEB, Chrome 의 경우 GET, POST 전에 OPTIONS을 통해 해당 메서드를 지원하는지 체크하는데 = PASS
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // js, html, png, resource 요청 -PASS
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        /*
         * Header 검증 로직
         * */
        // (1) 헤더에서 authorization-token 꺼내고
        String accessToken = request.getHeader("access-token");
        // 토큰이 없다면?
        if (accessToken == null) {
            String mockToken = request.getHeader("mock-token");
            // 단위 테스트 토큰
            if (mockToken == null) {
                throw new AuthException(ErrorType.TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND);
            }
            setRequestContext(Long.valueOf(mockToken));
            return true;
        }

        // (2) 토큰이 유효한지 체크 - 없다면 BAD_REQUEST 후, memberId 반환
        Long memberId = jwtTokenUtility.getAccessTokenMemberId(accessToken);
        setRequestContext(memberId);
        return true;
    }

    private void setRequestContext(Long memberId) {
        // (4)-1 현재 요청 request Context 에다가 memberId를 저장한다.
        // (4)-2 범위는 이번 요청동안만! SCOPE_REQUEST
        RequestAttributes requestContext = Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes());
        requestContext.setAttribute("memberId", memberId, RequestAttributes.SCOPE_REQUEST);
    }

}
