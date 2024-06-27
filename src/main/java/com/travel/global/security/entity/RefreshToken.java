package com.travel.global.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = @Index(columnList = "tokenValue"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(unique = true)
    private String tokenValue;

//    private String clientIp;
//
//    private String userAgent;

//    private LocalDateTime expiryDate;

    public RefreshToken(
        String email, String tokenValue
        ) {
        this.email = email;
        this.tokenValue = tokenValue;
//        this.clientIp = clientIp;
//        this.userAgent = userAgent;
    }

    public void update(String email, String tokenValue) {
        this.email = email;
        this.tokenValue = tokenValue;
    }
}
