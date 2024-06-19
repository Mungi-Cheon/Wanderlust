package com.travel.domain.accommodation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "accommodation_id")
  private Accommodation accommodation;

  private String thumbnail;
  private String imageUrl1;
  private String imageUrl2;

}
