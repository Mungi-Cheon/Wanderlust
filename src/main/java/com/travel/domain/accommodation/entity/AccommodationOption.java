package com.travel.domain.accommodation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "accommodation_id")
  private Accommodation accommodation;

  private boolean hasSmokingRoom;
  private boolean hasCooking;
  private boolean hasParking;
  private boolean hasSwimmingPool;
  private boolean hasBreakfast;
  private boolean hasFitness;
  private boolean hasBeauty;

}
