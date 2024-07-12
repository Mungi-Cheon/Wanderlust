package com.travel.domain.member.entity;

import com.travel.domain.member.dto.request.SignupRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String password;

    private String email;

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    public static Member from(SignupRequest request, String password) {
        return Member.builder()
            .email(request.getEmail())
            .name(request.getName())
            .password(password)
            .build();
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public Member withDeletedEmail() {
        String newEmail = "deleted-email-" + this.id + "@" + this.email.split("@")[1];
        return this.toBuilder().email(newEmail).build();
    }
}

