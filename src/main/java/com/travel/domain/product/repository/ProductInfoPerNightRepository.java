package com.travel.domain.product.repository;

import com.travel.domain.product.entity.ProductInfoPerNight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductInfoPerNightRepository extends JpaRepository<ProductInfoPerNight, Long> {


}