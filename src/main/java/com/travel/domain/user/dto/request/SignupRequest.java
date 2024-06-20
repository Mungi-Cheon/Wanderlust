package com.travel.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청")
public class SignupRequest {

    @Schema(description = "유저 이름")
    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String username;

    @Schema(description = "이메일")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    private String email;

    @Schema(description = "비밀번호")
//    @Size(min = 8, message = "8자리 이상의 비밀번호를 입력해 주세요")
    @NotBlank(message = "패스워드는 필수 입력 사항입니다.")
    private String password;
}
