package com.travel.global.security.jwt;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;
    private boolean isRegenerated = false;
    @Getter
    private String newAccessToken;
    @Getter
    private String newRefreshToken;

    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities,
        String newAccessToken, String newRefreshToken) {
        super(authorities);
        this.principal = principal;
        this.credentials = null;
        this.isRegenerated = true;
        this.newAccessToken = newAccessToken;
        this.newRefreshToken = newRefreshToken;
        super.setAuthenticated(true);
    }

    public static JwtAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new JwtAuthenticationToken(principal, credentials);
    }

    public static JwtAuthenticationToken authenticated(Object principal,
        Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, null, authorities);
    }

    public static JwtAuthenticationToken authenticated(Object principal,
        Collection<? extends GrantedAuthority> authorities, String newAccessToken,
        String newRefreshToken) {
        return new JwtAuthenticationToken(principal, authorities, newAccessToken, newRefreshToken);
    }

    public boolean isRegenerated() {
        return this.isRegenerated;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {return this.principal;}

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
            "토큰을 신뢰할 수 있도록 설정할 수 없습니다 - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
