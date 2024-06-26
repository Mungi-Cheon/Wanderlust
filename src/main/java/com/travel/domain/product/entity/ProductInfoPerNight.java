package com.travel.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.travel.domain.accommodation.entity.Accommodation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
public class ProductInfoPerNight {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    private LocalDate date;

    private int price;

    private int count;

    public ProductInfoPerNight(Long id, Product product, Accommodation accommodation,
        LocalDate date, int price, int count) {
        this.id = id;
        this.product = product;
        this.accommodation = accommodation;
        this.date = date;
        this.price = price;
        this.count = count;
    }

    public ProductInfoPerNight() {
    }

    public void decreaseCountByOne() {
        this.count--;
    }
}