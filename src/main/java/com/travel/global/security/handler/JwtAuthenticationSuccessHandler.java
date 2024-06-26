//package com.travel.global.security.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//        Authentication authentication) throws IOException, ServletException {
//        response.setStatus(HttpStatus.OK.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("status", HttpStatus.OK.value());
//        responseBody.put("message", "Authentication success");
//
//        objectMapper.writeValue(response.getWriter(), responseBody);
//    }
//}
