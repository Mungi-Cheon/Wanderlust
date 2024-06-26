package com.travel.domain.user.dto.response;

import com.travel.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(

    @Schema(example = "fastfam@google.com")
    String email, String name) {

    public static UserResponse from(User entity) {
        return new UserResponse(
            entity.getEmail(),
            entity.getUsername());
    }
}
