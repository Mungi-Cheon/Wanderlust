package com.travel.domain.product.dto.response;


import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.product.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationImageResponse {

    private String imageUrl1;
    private String imageUrl2;

    public static AccommodationImageResponse toResponse(AccommodationImage accommodationImage){
        return AccommodationImageResponse.builder()
            .imageUrl1(accommodationImage.getImageUrl1())
            .imageUrl2(accommodationImage.getImageUrl2())
            .build();
    }
}