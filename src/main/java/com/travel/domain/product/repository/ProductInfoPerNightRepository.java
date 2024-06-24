package com.travel.domain.product.repository;

import com.travel.domain.product.entity.ProductInfoPerNight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductInfoPerNightRepository extends JpaRepository<ProductInfoPerNight, Long> {

    //해당 date 범위에 해당하는 product
    @Query("SELECT p FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND :checkIn <= p.date " +
        "AND :checkOut > p.date " +
        "AND p.count >= 1")
    List<ProductInfoPerNight> findByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut);

    // 특정 productId와 date에 해당하는 레코드가 존재하는지 확인하고 count가 0보다 큰지도 검사
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
        "FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND p.date = :date " +
        "AND p.count > 0")
    boolean existsByProductIdAndDate(
        @Param("productId") Long productId,
        @Param("date") LocalDate date
    );

    // 주어진 날짜 범위 내에서 해당 product의 최소 count 값을 반환
    @Query("SELECT MIN(p.count) FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND p.date BETWEEN :checkIn AND :checkOut")
    Integer findMinCountByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut);
}