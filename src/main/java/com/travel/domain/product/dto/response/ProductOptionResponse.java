package com.travel.domain.product.dto.response;

import com.travel.domain.product.entity.ProductOption;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionResponse {
  private boolean hasBath;
  private boolean hasAirCondition;
  private boolean hasTv;
  private boolean hasPc;
  private boolean hasWifi;
  private boolean hasCable;
  private boolean hasRefrigerator;
  private boolean hasSofa;
  private boolean canCook;
  private boolean hasTable;
  private boolean hasHairdryer;

  public static ProductOptionResponse toResponse(ProductOption productOption) {
    return ProductOptionResponse.builder()
        .hasBath(productOption.isHasBath())
        .hasAirCondition(productOption.isHasAirCondition())
        .hasTv(productOption.isHasTv())
        .hasPc(productOption.isHasPc())
        .hasWifi(productOption.isHasWifi())
        .hasCable(productOption.isHasCable())
        .hasRefrigerator(productOption.isHasRefrigerator())
        .hasSofa(productOption.isHasSofa())
        .canCook(productOption.isCanCook())
        .hasTable(productOption.isHasTable())
        .hasHairdryer(productOption.isHasHairdryer())
        .build();
  }
}