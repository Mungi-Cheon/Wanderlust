package com.travel.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 요청")
public class LoginRequest {

    @Schema(description = "이메일")
    @Email
    private String email;

    @Schema(description = "비밀번호")
    private String password;
}
