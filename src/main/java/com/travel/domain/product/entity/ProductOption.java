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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @Column(columnDefinition = "bit")
    private Integer hasBath;
    @Column(columnDefinition = "bit")
    private Integer hasAirCondition;
    @Column(columnDefinition = "bit")
    private Integer hasTv;
    @Column(columnDefinition = "bit")
    private Integer hasPc;
    @Column(columnDefinition = "bit")
    private Integer hasWifi;
    @Column(columnDefinition = "bit")
    private Integer hasCable;
    @Column(columnDefinition = "bit")
    private Integer hasRefrigerator;
    @Column(columnDefinition = "bit")
    private Integer hasSofa;
    @Column(columnDefinition = "bit")
    private Integer canCook;
    @Column(columnDefinition = "bit")
    private Integer hasTable;
    @Column(columnDefinition = "bit")
    private Integer hasHairdryer;
}