package com.travel.domain.review.dto.response;

import com.travel.domain.accommodation.entity.Accommodation;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AccommodationReviewResponseList {

    private Long id;

    private String name;

    private String thumbnail;

    private BigDecimal grade;

    private List<ReviewResponse> reviewResponseList;

    public static AccommodationReviewResponseList from(Accommodation accommodation,
        List<ReviewResponse> reviewResponses) {
        String thumbnail = accommodation.getImages().getThumbnail();
        return AccommodationReviewResponseList.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .grade(accommodation.getGrade())
            .thumbnail(thumbnail)
            .reviewResponseList(reviewResponses)
            .build();
    }
}
