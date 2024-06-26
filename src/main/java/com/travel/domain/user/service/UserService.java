package com.travel.domain.user.service;

import com.travel.domain.user.dto.request.LoginRequest;
import com.travel.domain.user.dto.request.SignupRequest;
import com.travel.domain.user.dto.response.LoginDto;
import com.travel.domain.user.dto.response.UserResponse;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import com.travel.global.exception.UserException;
import com.travel.global.security.provider.JwtProvider;
import com.travel.global.security.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    public UserResponse join(SignupRequest request) {
        String email = request.getEmail();

        boolean isExist = userRepository.existsByEmail(email);

        if (isExist) { //error type 에서 보고 수정
            throw new UserException(HttpStatusCode.valueOf(400), "이미 존재하는 사용자입니다.");
        }
        String password = passwordEncoder.encode(request.getPassword());
        User entity = User.from(request, password);

        User savedUser = userRepository.save(entity);

        return UserResponse.from(savedUser);
    }

    public LoginDto login(LoginRequest request) {
        User user = userRepository.findByEmailOrderByIdDesc(request.getEmail())
            .orElseThrow(() -> new UserException(HttpStatusCode.valueOf(400),
                "사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException(HttpStatusCode.valueOf(400),
                "비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = refreshTokenService.updateRefreshToken(user.getEmail());
        return new LoginDto(accessToken, refreshToken);
    }
    // 클라이언트의 쿠키에 토큰을 저장하는 메서드
//    private void saveTokenInCookie(HttpServletResponse response, String accessToken) {
//        Cookie cookie = new Cookie("access-token", accessToken);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(60 * 24); // 24시간 유효한 쿠키 설정
//        response.addCookie(cookie);
//    }
}
