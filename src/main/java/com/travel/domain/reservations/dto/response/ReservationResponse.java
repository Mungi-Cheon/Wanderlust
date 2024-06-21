package com.travel.domain.reservations.dto.response;

import com.travel.domain.reservations.entity.Reservations;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponse {

    private Long id;
    private Integer personNumber;
    private Integer totalPrice;
    private Integer price;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public static ReservationResponse from(Reservations entity) {
        return ReservationResponse.builder()
            .id(entity.getId())
            .personNumber(entity.getPersonNumber())
            .totalPrice(calcTotalPrice(entity.getPrice(), entity.getCheckInDate(),
                entity.getCheckOutDate()))
            .price(entity.getPrice())
            .checkInDate(entity.getCheckInDate())
            .checkOutDate(entity.getCheckOutDate())
            .build();
    }

    private static int calcTotalPrice(int price, LocalDate checkInDate, LocalDate checkOutDate) {
        int nights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return price * nights;
    }


}
