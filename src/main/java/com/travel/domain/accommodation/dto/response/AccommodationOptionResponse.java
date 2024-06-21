package com.travel.domain.accommodation.dto.response;

import com.travel.domain.accommodation.entity.AccommodationOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationOptionResponse {

  private boolean hasSmokingRoom;
  private boolean hasCooking;
  private boolean hasParking;
  private boolean hasSwimmingPool;
  private boolean hasBreakfast;
  private boolean hasFitness;
  private boolean hasBeauty;

  public static AccommodationOptionResponse toResponse(AccommodationOption accommodationOption) {
    return AccommodationOptionResponse.builder()
        .hasSmokingRoom(accommodationOption.isHasSmokingRoom())
        .hasCooking(accommodationOption.isHasCooking())
        .hasParking(accommodationOption.isHasParking())
        .hasSwimmingPool(accommodationOption.isHasSwimmingPool())
        .hasBreakfast(accommodationOption.isHasBreakfast())
        .hasFitness(accommodationOption.isHasFitness())
        .hasBeauty(accommodationOption.isHasBeauty())
        .build();
  }
}
