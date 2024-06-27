package com.travel.domain.user.entity;

import com.travel.domain.user.dto.request.SignupRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    private String email;

    public static User from(
        SignupRequest request, String password) {
        return User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .password(password)
            .build();
    }
}
