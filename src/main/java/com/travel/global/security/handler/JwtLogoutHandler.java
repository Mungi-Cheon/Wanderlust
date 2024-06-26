package com.travel.global.security.handler;

import com.travel.global.security.service.RefreshTokenService;
import com.travel.global.util.cookie.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtLogoutHandler implements LogoutHandler {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<Cookie> refreshTokenCookie = CookieUtil.getCookie(request, "refresh-token");
        refreshTokenCookie.ifPresent(cookie -> {
            String refreshToken = cookie.getValue();
            refreshTokenService.deleteByTokenValue(refreshToken);
        });

        CookieUtil.deleteCookie(request, response, "access-token");
        CookieUtil.deleteCookie(request, response, "refresh-token");
    }
}

