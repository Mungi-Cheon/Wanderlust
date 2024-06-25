package com.travel.domain.accommodation.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.travel.domain.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(precision = 2, scale = 1)
    private BigDecimal grade;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "accommodation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private AccommodationOption options;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "accommodation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private AccommodationImage images;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accommodation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products;

}