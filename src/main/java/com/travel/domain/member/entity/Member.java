package com.travel.domain.member.entity;

import com.travel.domain.member.dto.request.SignupRequest;
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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String password;

    private String email;

    public static Member from(
        SignupRequest request, String password) {
        return Member.builder()
            .email(request.getEmail())
            .name(request.getName())
            .password(password)
            .build();
    }
}
