package com.travel.global.security.service;

import com.travel.global.security.entity.RefreshToken;

import com.travel.global.security.repositroy.RefreshTokenRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String updateRefreshToken(String email) {

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(email);

        String refreshTokenValue = UUID.randomUUID().toString();

//        LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);
//
//        String clientIp = details.getClientIp();
//
//        String userAgent = details.getUserAgent();

        if (refreshToken.isPresent()) {
            refreshToken.get().update(email, refreshTokenValue);
            refreshTokenRepository.saveAndFlush(refreshToken.get());

        } else {
            refreshTokenRepository.saveAndFlush(
                new RefreshToken(email, refreshTokenValue));
        }
        return refreshTokenValue;
    }

    @Transactional
    public void deleteByTokenValue(String tokenValue) {
        refreshTokenRepository.deleteByTokenValue(tokenValue);
    }

    public boolean isStoredRefreshToken(String refreshToken, String email) {
        return refreshTokenRepository.findByEmail(email).isPresent();
    }
}
