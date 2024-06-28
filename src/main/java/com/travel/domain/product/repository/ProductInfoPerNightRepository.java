package com.travel.domain.product.repository;

import com.travel.domain.product.entity.ProductInfoPerNight;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductInfoPerNightRepository extends JpaRepository<ProductInfoPerNight, Long> {

    @Query("SELECT p FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND :checkInDate <= p.date " +
        "AND :checkOutDate > p.date " +
        "AND p.count > 0")
    List<ProductInfoPerNight> findByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
        "FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND p.date = :date " +
        "AND p.count > 0")
    boolean existsByProductIdAndDate(
        @Param("productId") Long productId,
        @Param("date") LocalDate date
    );

    @Query("SELECT MIN(p.count) FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND p.date BETWEEN :checkInDate AND :checkOutDate")
    Integer findMinCountByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);
}