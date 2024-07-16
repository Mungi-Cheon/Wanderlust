package com.travel.domain.review.repository;

import com.travel.domain.review.entity.Review;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.type.ErrorType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  Optional<Review> findByIdAndAccommodationId(Long reviewId, Long accommodationId);

  boolean existsByReservationId(Long reservationId);

  List<Review> findByAccommodationId(Long accommodationId);

  /*default List<Review> getByAccommodationId(Long accommodationId) {
    List<Review> reviews = findByAccommodationId(accommodationId);
    if (reviews.isEmpty()) {
      throw new AccommodationException(ErrorType.NOT_FOUND);
    }
    return reviews;
  }*/
}