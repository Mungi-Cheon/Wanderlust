package com.travel.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorType {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    AUTH_NOT_EXIST(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "조회 결과가 없습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),
    ALREADY_RESERVATION(HttpStatus.CONFLICT, "이미 예약된 숙박 정보가 존재합니다."),
    EMPTY_ACCOMMODATION(HttpStatus.NOT_FOUND, "숙소 정보가 없습니다"),
    EMPTY_PRODUCT(HttpStatus.NOT_FOUND," 숙박 정보가 없습니다"),
    INVALID_NUMBER_OF_PEOPLE(HttpStatus.BAD_REQUEST, "잘못된 인원")
        ;

    private final HttpStatusCode statusCode;
    private final String message;
}
