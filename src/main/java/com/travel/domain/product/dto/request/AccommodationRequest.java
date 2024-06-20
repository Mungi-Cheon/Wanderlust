package com.travel.domain.product.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

//TODO accommodation package  로 이동
@Getter
public class AccommodationRequest {

    @NotNull(message = "체크인 날짜를 지정해주세요")
    private LocalDate checkIn;

    @NotNull(message = "체크인 아웃 지정해주세요")
    private LocalDate checkOut;

    @NotNull(message = "인원을 선택해주세요")
    private int personnel;
    //categoty 등 필요한 것들 추후 추가

    @AssertTrue(message = "체크인 전 체크아웃을 할 수 없습니다.")
    public boolean isValidPeriod() {
        return checkOut.isAfter(checkIn) || checkIn.isEqual(checkOut);
    }

    public AccommodationRequest(LocalDate checkIn, LocalDate checkOut, Integer personnel) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.personnel = personnel;
    }
}
