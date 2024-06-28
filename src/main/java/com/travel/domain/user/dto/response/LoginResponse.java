package com.travel.domain.user.dto.response;

import com.travel.domain.user.entity.User;

public record LoginResponse(

    String accessToken
) {
    public static LoginResponse from(String accessToken) {
        return new LoginResponse(
            accessToken);
    }
}
