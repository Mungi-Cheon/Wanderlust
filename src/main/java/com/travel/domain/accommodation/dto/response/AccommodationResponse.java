package com.travel.domain.accommodation.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationResponse {

  private Long id;
  private String name;
  private Integer price;
  private String thumbnail;
  private String category;
  private Double grade;

}
