package com.travel.domain.product.dto.response;


import com.travel.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter  //count 추가
public class ProductDetailResponse {

    private Long id;

    private String name;

    private String accommodationName;

    private String description;

    private int totalPrice;

    private int pricePerNight;

    private int numberOfStay;

    private int standardNumber;

    private int maximumNumber;

    private int count;

    private String type;

    private ProductImageResponse productImageResponse;

    private ProductOptionResponse productOption;


    public static ProductDetailResponse from(Product product, String accommodationName,
        int pricePerNight, int totalPrice,
        int numberOfStay, ProductImageResponse productImageResponse,
        ProductOptionResponse productOptionResponse) {
        return ProductDetailResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .accommodationName(accommodationName)
            .description(product.getDescription())
            .pricePerNight(pricePerNight)
            .totalPrice(totalPrice)
            .numberOfStay(numberOfStay)
            .standardNumber(product.getStandardNumber())
            .maximumNumber(product.getMaximumNumber())
            .count(numberOfStay)
            .type(product.getType())
            .productImageResponse(productImageResponse)
            .productOption(productOptionResponse)
            .build();
    }
}