package com.travel.domain.user.service;

import static com.travel.global.exception.type.ErrorType.DUPLICATED_USER;
import static com.travel.global.exception.type.ErrorType.NOT_CORRECT_PASSWORD;

import com.travel.domain.user.dto.request.LoginRequest;
import com.travel.domain.user.dto.request.SignupRequest;
import com.travel.domain.user.dto.response.LoginDto;
import com.travel.domain.user.dto.response.UserResponse;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import com.travel.global.exception.UserException;
import com.travel.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public UserResponse join(SignupRequest request) {
        String email = request.getEmail();

        boolean isExist = userRepository.existsByEmail(email);

        if (isExist) { //error type 에서 보고 수정
            throw new UserException(DUPLICATED_USER);
        }
        String password = passwordEncoder.encode(request.getPassword());
        User entity = User.from(request, password);

        User savedUser = userRepository.save(entity);
        return UserResponse.from(savedUser);
    }

    public LoginDto login(LoginRequest request) {
        User user = userRepository.findByEmailOrderByIdDesc(request.getEmail())
            .orElseThrow(() -> new UserException(DUPLICATED_USER));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException(NOT_CORRECT_PASSWORD);
        }

        String accessToken = jwtProvider.generateAccessToken(user.getId());
        return new LoginDto(accessToken);
    }
}
