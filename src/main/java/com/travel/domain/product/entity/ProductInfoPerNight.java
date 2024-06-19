package com.travel.domain.product.entity;

import com.travel.domain.accommodation.entity.Accommodation;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name ="product_info_per_night")
public class ProductInfoPerNight {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "accommodation_id")
  private Accommodation accommodation;

  @ManyToOne
  @JoinColumn(name =" product_id")
  private Product product;


  private LocalDate date;
  private int price;
  private int count;
}
