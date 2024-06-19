package com.travel.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.domain.accommodation.entity.Accommodation;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name ="product_image")
public class ProductImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "accommodation_id")
  private Accommodation accommodation;

  @ManyToOne
  @JoinColumn(name =" product_id")
  private Product product;

  private String imageUrl1;
  private String imageUrl2;

}
