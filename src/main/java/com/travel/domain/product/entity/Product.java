package com.travel.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.domain.accommodation.entity.Accommodation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name ="product") //객실
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String checkInTime;
    private String checkOutTime; // 16:00
    private int standardNumber;
    private int maximumNumber;
    private String type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private ProductOption productOption;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductInfoPerNight> productInfoPerNightsList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="id")
    private ProductImage productImage;

}
