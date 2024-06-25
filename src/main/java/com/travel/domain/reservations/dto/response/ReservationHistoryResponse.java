package com.travel.domain.reservations.dto.response;

import com.travel.domain.reservations.entity.Reservation;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder // 어노테이션 정리, from 메서드 인자 개행 정리
public class ReservationHistoryResponse {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer night;

    private Integer personNumber;

    private String accommodationName;

    private String roomType;

    private Integer standardNumber;

    private Integer maximumNumber;

    private String imageUrl;

    private Integer price;

    private Integer totalPrice;

    public static ReservationHistoryResponse from(Reservation entity, String accommodationName
        , String roomType, Integer standardNumber, Integer maximumNumber, String imageUrl) {
        return ReservationHistoryResponse.builder()
            .id(entity.getId())
            .checkInDate(entity.getCheckInDate())
            .checkOutDate(entity.getCheckOutDate())
            .night(entity.getNight())
            .personNumber(entity.getPersonNumber())
            .accommodationName(accommodationName)
            .roomType(roomType)
            .standardNumber(standardNumber)
            .maximumNumber(maximumNumber)
            .imageUrl(imageUrl)
            .price(entity.getPrice())
            .totalPrice(calcTotalPrice(entity.getPrice(), entity.getNight()))
            .build();
    }

    private static int calcTotalPrice(int price, int night) {
        return price * night;
    }
}
