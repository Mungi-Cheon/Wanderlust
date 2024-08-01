package com.travel.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CookieUtil {

    public void createAccessTokenCookie(HttpServletResponse response, String tokenName,
        String token) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }

    public void createRefreshTokenCookie(HttpServletResponse response, String tokenName,
        String token) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(2592000);
        response.addCookie(cookie);
    }

    public String getTokenFromCookies(Cookie[] cookies, String tokenName) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
            .filter(cookie -> tokenName.equals(cookie.getName()))
            .map(Cookie::getValue)
            .findAny()
            .orElse(null);
    }

    public void removeCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .sameSite("None")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}