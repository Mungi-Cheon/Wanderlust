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
    private Integer hasBath;
    private Integer hasAirCondition;
    private Integer hasTv;
    private Integer hasPc;
    private Integer hasWifi;
    private Integer hasCable;
    private Integer hasRefrigerator;
    private Integer hasSofa;
    private Integer canCook;
    private Integer hasTable;
    private Integer hasHairdyer;

    public static ProductOptionResponse toResponse(ProductOption productOption){
        return ProductOptionResponse.builder()
            .hasBath(productOption.getHasBath())
            .hasAirCondition(productOption.getHasAirCondition())
            .hasTable(productOption.getHasTable())
            .hasWifi(productOption.getHasWifi())
            .hasCable(productOption.getHasCable())
            .hasRefrigerator(productOption.getHasRefrigerator())
            .hasSofa(productOption.getHasSofa())
            .canCook(productOption.getCanCook())
            .hasTable(productOption.getHasTable())
            .hasHairdyer(productOption.getHasHairdyer())
            .build();
    }
}