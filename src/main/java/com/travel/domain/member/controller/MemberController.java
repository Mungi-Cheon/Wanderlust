package com.travel.domain.member.controller;

import com.travel.domain.member.dto.request.LoginRequest;
import com.travel.domain.member.dto.request.SignupRequest;
import com.travel.domain.member.dto.response.LoginDto;
import com.travel.domain.member.dto.response.MemberResponse;
import com.travel.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "회원 정보를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원 가입 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = MemberResponse.class)))
    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(
        @RequestBody @Valid SignupRequest signupRequest) {
        MemberResponse response = memberService.signup(signupRequest);
        return ResponseEntity.accepted().body(response);
    }

    @Operation(summary = "로그인 ", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponse(description = "로그인 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = LoginDto.class)))
    @PostMapping("/login")
    public ResponseEntity<Void> login(
        @RequestBody @Valid LoginRequest loginRequest,
        HttpServletResponse response){
        LoginDto loginDto = memberService.login(loginRequest);
        response.setHeader("access-token", loginDto.accessToken());
        return ResponseEntity.ok().build();
    }
}
