package com.travel.domain.review.repository;

import com.travel.domain.review.entity.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Optional<Review> findByIdAndAccommodationId(Long reviewId, Long accommodationId);
  boolean existsByReservationId(Long reservationId);

}