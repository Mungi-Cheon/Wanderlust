package com.travel.domain.product.dto.response;


import com.travel.domain.accommodation.entity.AccommodationImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private List <ProductResponse> productResponseList;
    private int count;

}