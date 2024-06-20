package com.travel.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorType {

    BAD_REQUEST(400, "잘못된 요청입니다."),
    AUTH_NOT_EXIST(401, "권한이 없습니다."),
    SERVER_ERROR(500, "서버 에러입니다."),

    EMPTY_ACCOMMODATION(400, "숙소 정보가 없습니다"),

    INVALID_NUMBER_OF_PEOPLE(400, "잘못된 인원")
    ;

    private final int statusCode;
    private final String message;
}
