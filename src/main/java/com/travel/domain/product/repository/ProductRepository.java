package com.travel.domain.product.repository;

import com.travel.domain.product.entity.Product;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByAccommodationId(Long accommodationId);

    Optional<Product> findByIdAndAccommodationId(Long productId, Long accommodationId);
}