package com.travel.domain.product.dto.response;


import com.travel.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
  private Long id;
  private String name;
  private String accommodationName;
  private String description;
  private int totalPrice;
  private int pricePerNight;// 1박당 가격
  private int numberOfStay; //숙박일수
  private int standardNumber;
  private int maximumNumber;
  private String type;
  private ProductImageResponse productImageResponse;
  private ProductOptionResponse productOption; // 편의시설

  public static ProductDetailResponse from(Product product, String accommodationName, int pricePerNight, int totalPrice, int numberOfStay, ProductImageResponse productImageResponse, ProductOptionResponse productOptionResponse) {
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
        .type(product.getType())
        .productImageResponse(productImageResponse)
        .productOption(productOptionResponse)
        .build();
  }
}