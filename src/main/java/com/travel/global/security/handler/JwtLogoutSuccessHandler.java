package com.travel.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.OK.value());
        responseBody.put("message", "logout success");

        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}
