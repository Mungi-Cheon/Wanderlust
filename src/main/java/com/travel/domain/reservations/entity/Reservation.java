package com.travel.domain.reservations.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.product.entity.Product;
import com.travel.domain.member.entity.Member;
import com.travel.domain.review.entity.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer personNumber;

    private Integer price;

    private Integer night;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    @JsonIgnore
    private Accommodation accommodation;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reservation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Review> reviews;
}
