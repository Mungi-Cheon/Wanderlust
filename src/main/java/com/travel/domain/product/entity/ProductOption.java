package com.travel.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.domain.accommodation.entity.Accommodation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name ="product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private Integer hasRefigerator; // hasRefrigerator
    @Column(columnDefinition = "bit")
    private Integer hasSofa;
    @Column(columnDefinition = "bit")
    private Integer canCook;
    @Column(columnDefinition = "bit")
    private Integer hasTable;
    @Column(columnDefinition = "bit")
    private Integer hasHairdyer;    //hasHairDyer
}


 /* @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accomodation;
*/
