package com.travel.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.global.config.ObjectMapperConfig;
import com.travel.global.exception.type.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {

        ObjectMapper objectMapper = new ObjectMapperConfig().objectMapper();
        var responseResult = ResponseEntity.status(
                ErrorType.TOKEN_AUTHORIZATION_FAIL.getStatusCode())
            .body(ErrorType.TOKEN_AUTHORIZATION_FAIL.getMessage());
        String responseBody = objectMapper.writeValueAsString(responseResult);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(responseBody);
        response.getWriter().flush();

        authException.printStackTrace();
    }
}