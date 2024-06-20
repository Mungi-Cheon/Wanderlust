package com.travel.domain.product.dto.response;


import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.entity.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
  private ProductOption productOption; // 편의시설
}