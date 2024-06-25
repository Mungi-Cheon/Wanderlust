package com.travel.domain.accommodation.dto.response;

import com.travel.domain.accommodation.entity.Accommodation;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AccommodationResponse {

    private Long id;

    private String name;

    private Integer price;

    private String thumbnail;


    private String category;

    private BigDecimal grade;

    public static AccommodationResponse from(Accommodation accommodation, int price) {
        String thumbnail = accommodation.getImages().getThumbnail();

        return AccommodationResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .price(price)
            .thumbnail(thumbnail)
            .category(accommodation.getCategory())
            .grade(accommodation.getGrade())
            .build();
    }

}