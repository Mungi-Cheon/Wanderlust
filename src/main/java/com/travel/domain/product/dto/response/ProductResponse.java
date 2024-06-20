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
  private int pricePerNight;// 1박당 가격
  private int standardNumber;
  private int maximumNumber;
  private ProductImage images;
  private int count;

  public static ProductResponse toResponse(Product product, int count){
    return ProductResponse.builder()
        .name(product.getName())
        .checkInTime(product.getCheckInTime())
        .checkOutTime(product.getCheckOutTime())
        .pricePerNight(product.getProductInfoPerNightsList().get(0).getPrice())
        .standardNumber(product.getStandardNumber())
        .maximumNumber(product.getMaximumNumber())
        .images(product.getProductImage())
        .count(count)
        .build();
  }
}