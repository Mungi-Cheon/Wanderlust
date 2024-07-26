package com.travel.domain.cart.dto.response;

import com.travel.domain.cart.entity.Cart;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartResponse {
    private String accommodationName;

    private String productName;

    private String productImageUrl;

    private int productStandardNumber;

    private int productMaximumNumber;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private int personNumber;

    private int price;

    private int nights;

    private int totalPrice;

    public static CartResponse from(Cart cart) {
        int nights = (int) ChronoUnit.DAYS.between(cart.getCheckInDate(), cart.getCheckOutDate());
        int price = cart.getProduct().getProductInfoPerNightsList().get(0).getPrice();

        return CartResponse.builder()
            .accommodationName(cart.getAccommodation().getName())
            .productName(cart.getProduct().getName())
            .productImageUrl(cart.getProduct().getProductImage().getImageUrl1())
            .productStandardNumber(cart.getProduct().getStandardNumber())
            .productMaximumNumber(cart.getProduct().getMaximumNumber())
            .checkInDate(cart.getCheckInDate())
            .checkOutDate(cart.getCheckOutDate())
            .personNumber(cart.getPersonNumber())
            .price(price)
            .nights(nights)
            .totalPrice(price * nights)
            .build();
    }
}
