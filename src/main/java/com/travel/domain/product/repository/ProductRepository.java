package com.travel.domain.product.repository;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByAccommodationId(Long accommodationId);

    Optional <Product>findByType(String roomType);

    List<Product> findAllByAccommodationId(Long accommodationId);
}