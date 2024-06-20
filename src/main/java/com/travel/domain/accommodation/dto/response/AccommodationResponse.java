package com.travel.domain.accommodation.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationResponse {

  private Long id;
  private String name;
  private Integer price;
  private String thumbnail;
  private String category;
  private BigDecimal grade;

}
