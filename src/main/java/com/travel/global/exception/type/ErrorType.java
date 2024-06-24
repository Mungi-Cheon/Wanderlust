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
    INVALID_NUMBER_OF_PEOPLE(HttpStatus.BAD_REQUEST, "잘못된 인원"),
    INCLUDES_FULLY_BOOKED_PRODUCT(HttpStatus.BAD_REQUEST, "예약 마감된 객실이 포함되어 있습니다."),
    INVALID_CHECK_IN(HttpStatus.BAD_REQUEST, "체크인 날짜는 오늘 또는 이후 날짜여야 합니다."),
    INVALID_CHECK_OUT(HttpStatus.BAD_REQUEST, "체크아웃 날짜는 체크인 날짜 이후여야 합니다.");


    private final HttpStatusCode statusCode;
    private final String message;
}