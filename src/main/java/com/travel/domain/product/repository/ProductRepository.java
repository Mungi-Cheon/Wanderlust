package com.travel.domain.product.repository;

import com.travel.domain.product.entity.Product;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.type.ErrorType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByAccommodationId(Long accommodationId);

    @Query("SELECT p FROM Product p "
        + "LEFT JOIN FETCh p.productOption "
        + "LEFT JOIN FETCH p.productImage "
        + "WHERE p.id = :id")
    Optional<Product> findByIdJoinImagesAndOption(@Param("id") Long id);

    Optional<Product> findByIdAndAccommodationId(Long productId, Long accommodationId);

    @Query("SELECT p FROM Product p " +
        "LEFT JOIN FETCH p.productOption " +
        "LEFT JOIN FETCH p.productImage " +
        "WHERE p.accommodation.id = :accommodationId")
    List<Product> findAllByAccommodationIdWithFetchJoin
        (@Param("accommodationId") Long accommodationId);

    default Product getByIdJoinImagesAndOption(Long id) {
        return findByIdJoinImagesAndOption(id)
            .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));
    }
}