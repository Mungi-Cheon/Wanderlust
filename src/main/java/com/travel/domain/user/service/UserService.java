package com.travel.domain.user.service;

import com.travel.domain.user.dto.request.LoginRequest;
import com.travel.domain.user.dto.request.SignupRequest;
import com.travel.domain.user.dto.response.LoginResponse;
import com.travel.domain.user.dto.response.UserResponse;
import com.travel.domain.user.entity.UserEntity;
import com.travel.domain.user.repository.UserRepository;
import com.travel.global.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse join(SignupRequest request) {
        String email = request.getEmail();
        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            throw new UserException(HttpStatusCode.valueOf(400), "이미 존재하는 사용자입니다.");
        }

        UserEntity entity = UserEntity.from(request, passwordEncoder);
        UserEntity savedUser = userRepository.save(entity);
        return UserResponse.from(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserException(HttpStatusCode.valueOf(400), "사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException(HttpStatusCode.valueOf(400), "비밀번호가 일치하지 않습니다.");
        }

        // 실제 구현에서는 JWT 등의 토큰 생성 로직이 필요합니다.
        String accessToken = "generated-jwt-token";
        return new LoginResponse(accessToken);
    }
}
