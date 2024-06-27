package com.travel.domain.user.controller;

import com.travel.domain.user.dto.request.LoginRequest;
import com.travel.domain.user.dto.request.SignupRequest;
import com.travel.domain.user.dto.response.LoginResponse;
import com.travel.domain.user.dto.response.UserResponse;
import com.travel.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "회원 정보를 등록합니다.")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "회원 가입 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserResponse.class))))
    public ResponseEntity<UserResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {
        UserResponse response = userService.join(signupRequest);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 ", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "로그인 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = LoginResponse.class))))
    public ResponseEntity<LoginResponse> login(
        @RequestBody @Valid LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        LoginResponse response = userService.login(loginRequest,httpServletResponse);
        return ResponseEntity.ok(response);
    }
}
