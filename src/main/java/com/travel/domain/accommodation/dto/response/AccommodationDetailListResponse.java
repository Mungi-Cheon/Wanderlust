package com.travel.domain.accommodation.dto.response;


import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.product.dto.response.ProductResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AccommodationDetailListResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private AccommodationImageResponse accommodationImage;
    private AccommodationOptionResponse accommodationOption;
    private List<ProductResponse> productResponseList;

    public static AccommodationDetailListResponse from(Accommodation accommodation,
        LocalDate checkInDate,
        LocalDate checkOutDate, AccommodationImageResponse accommodationImage,
        AccommodationOptionResponse accommodationOption,
        List<ProductResponse> productResponseList) {
        return AccommodationDetailListResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .description(accommodation.getDescription())
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .accommodationImage(accommodationImage)
            .accommodationOption(accommodationOption)
            .productResponseList(productResponseList)
            .build();
    }
}