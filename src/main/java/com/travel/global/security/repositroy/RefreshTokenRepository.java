package com.travel.global.security.repositroy;

import com.travel.global.security.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

//    Optional<RefreshToken> findByClientIpAndUserAgent(String clientIp, String userAgent);

    Optional<RefreshToken> findByEmail(String email);

    Optional<RefreshToken> findByTokenValue(String tokenValue);

    void deleteByTokenValue(String tokenValue);
}
