package com.travel.domain.product.repository;

import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductInfoPerNightRepository extends JpaRepository<ProductInfoPerNight, Long> {

    //해당 date 범위에 해당하는
    @Query("SELECT p FROM ProductInfoPerNight p " +
        "WHERE p.product = :productId " +
        "AND :checkIn <= p.date " +
        "AND :checkOut >= p.date")
    Optional<ProductInfoPerNight> findByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut);

    //최저가
    @Query("SELECT MIN(p.price) FROM ProductInfoPerNight p " +
        "WHERE p.product = :productId " +
        "AND p.date BETWEEN :checkin AND :checkout")
    Integer findMinPriceByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkin") LocalDate checkin,
        @Param("checkout") LocalDate checkout);

    //total price
    @Query("SELECT SUM(p.price) FROM ProductInfoPerNight p " +
        "WHERE p.product = :productId " +
        "AND p.date BETWEEN :checkIn AND :checkOut")
    Integer findTotalPriceByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut);

    //숙박일수
    @Query("SELECT COUNT(p) FROM ProductInfoPerNight p WHERE p.date BETWEEN :checkIn AND :checkOut AND p.product.id = :productId")
    int findByDateBetweenAndProduct(
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut,
        @Param("productId") Long productId
    );

}