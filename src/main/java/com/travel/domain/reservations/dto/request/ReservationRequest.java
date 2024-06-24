package com.travel.domain.reservations.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;


@Getter
public class ReservationRequest {

    @NotNull
    private long accommodationId;
    @NotNull
    private long productId;
    @NotNull(message = "체크인 날짜를 입력해주세요.")
    private LocalDate checkInDate;
    @NotNull(message = "체크아웃 날짜를 입력해주세요.")
    private LocalDate checkOutDate;
    @Min(value = 1, message = "숙박 인원 수는 최소 1명 이상이어야 합니다.")
    private Integer personNumber;

    @AssertTrue(message = "체크인 날짜는 오늘 또는 이후 날짜여야 합니다.")
    public boolean isCheckInValid() {
        return checkInDate != null && !checkInDate.isBefore(LocalDate.now());
    }

    @AssertTrue(message = "체크아웃 날짜는 체크인 날짜 이후여야 합니다.")
    public boolean isCheckOutValid() {
        return checkInDate != null && checkOutDate != null && checkOutDate.isAfter(checkInDate);
    }
}
