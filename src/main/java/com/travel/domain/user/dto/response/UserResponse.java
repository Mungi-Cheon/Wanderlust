package com.travel.domain.user.dto.response;

import com.travel.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


public record UserResponse(

    @Schema(example = "fastfam@google.com")
    String email, String name) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getEmail(),
            user.getUsername());
    }
}
