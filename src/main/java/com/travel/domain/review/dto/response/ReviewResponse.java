package com.travel.domain.review.dto.response;

import com.travel.domain.review.entity.Review;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ReviewResponse {

    private Long id;

    private String accommodationName;

    private Long reservationId;

    private String title;

    private String comment;

    private BigDecimal grade;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static ReviewResponse from(Review review){
        return ReviewResponse.builder()
            .id(review.getId())
            .title(review.getTitle())
            .accommodationName(review.getAccommodation().getName())
            .reservationId(review.getReservation().getId())
            .title(review.getTitle())
            .comment(review.getComment())
            .grade(review.getGrade())
            .createdAt(LocalDateTime.now())
            .updatedAt(review.getUpdatedAt())
            .build();
    }
}
