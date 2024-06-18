package com.travel.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorType {

    BAD_REQUEST(400, "잘못된 요청입니다."),
    AUTH_NOT_EXIST(401, "권한이 없습니다."),
    SERVER_ERROR(500, "서버 에러입니다."),
    ;

    private final int statusCode;
    private final String message;
}
