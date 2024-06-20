package com.travel.domain.user.entity;

import com.travel.domain.user.dto.request.SignupRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter(value = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String password;

    private String email;

    @Builder
    public UserEntity(long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserEntity from(SignupRequest request,
        PasswordEncoder passwordEncoder) {
        UserEntity entity = new UserEntity();
        entity.setEmail(request.getEmail());
        entity.setName(request.getUsername());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        return entity;
    }
}

