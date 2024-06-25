package com.travel.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.travel.domain.accommodation.entity.Accommodation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String checkInTime;

    private String checkOutTime;

    private int standardNumber;

    private int maximumNumber;

    private String type;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private ProductOption productOption;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductInfoPerNight> productInfoPerNightsList;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private ProductImage productImage;
}