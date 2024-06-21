package com.travel.domain.reservations.dto.request;

import java.time.LocalDate;
import lombok.Getter;


@Getter
public class ReservationRequest {

    private long accommodationId;
    private long productId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer price;
    private Integer personNumber;
}
