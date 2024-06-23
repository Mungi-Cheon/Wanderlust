package com.travel.domain.accommodation.dto.response;


import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.product.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDetailListResponse {
    private Long id;
    private String name;
    private String description;
    private String checkIn;
    private String checkOut;
    private AccommodationImageResponse accommodationImage;
    private AccommodationOptionResponse accommodationOption;
    private List <ProductResponse> productResponseList;

    public static AccommodationDetailListResponse toResponse(Accommodation accommodation, String checkIn, String checkOut, AccommodationImageResponse accommodationImage, AccommodationOptionResponse accommodationOption, List<ProductResponse> productResponseList) {
        return AccommodationDetailListResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .description(accommodation.getDescription())
            .checkIn(checkIn)
            .checkOut(checkOut)
            .accommodationImage(accommodationImage)
            .accommodationOption(accommodationOption)
            .productResponseList(productResponseList)
            .build();
    }
}