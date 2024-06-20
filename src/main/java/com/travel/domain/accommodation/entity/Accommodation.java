package com.travel.domain.accommodation.entity;

import com.travel.domain.product.entity.Product;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Accommodation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String contact;

  @Column(columnDefinition = "TEXT")
  private String description;

  private String address;

  private String category;

  private Double grade;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "accommodation", cascade = CascadeType.ALL)
  private AccommodationOption options;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "accommodation", cascade = CascadeType.ALL)
  private AccommodationImage images;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "accommodation", cascade = CascadeType.ALL)
  private List<Product> products;

}
