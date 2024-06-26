package com.travel.global.security.service;

import com.travel.KdtBe8MiniProjectApplication;
import com.travel.global.exception.UserException;
import com.travel.global.exception.type.ErrorType;
import com.travel.global.security.entity.RefreshToken;

import com.travel.global.security.repositroy.RefreshTokenRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
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
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email)
            // todo refactoring "" 애초에 임시 ;;;
            // todo: insert한번 나가게 없다면 save() - 1개 줄이는거니까. 고려
            .orElse(refreshTokenRepository.save(new RefreshToken(email,"")));

        refreshToken.update(email, refreshTokenValue);
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
