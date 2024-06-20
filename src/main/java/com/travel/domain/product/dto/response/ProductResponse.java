package com.travel.domain.product.dto.response;


import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductOption;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private String name;
    private String checkInTime;
    private String checkOutTime;
    private String pricePerNight;// 1박당 가격
    private int standardNumber;
    private int maximumNumber;
    private ProductOption productOption;
    private ProductImage images;

    //TODO 옵션 제거
    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
            .name(product.getName())
            .checkInTime(product.getCheckInTime())
            .checkOutTime(product.getCheckOutTime())
            .standardNumber(product.getStandardNumber())
            .maximumNumber(product.getMaximumNumber())
            .images(product.getProductImage())
            .build();
    }
}


