package com.travel.domain.reservations.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.product.entity.Product;
import com.travel.domain.user.entity.UserEntity;
import jakarta.persistence.Entity;
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

@Getter
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
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    @JsonIgnore
    private Accommodation accommodation;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    // 인자 개행
    @Builder
    public Reservation(
        UserEntity user, Accommodation accommodation,
        Product product, Integer personNumber,
        Integer price, Integer night,
        LocalDate checkInDate, LocalDate checkOutDate) {
        this.user = user;
        this.accommodation = accommodation;
        this.product = product;
        this.personNumber = personNumber;
        this.price = price;
        this.night = night;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}
