package com.travel.domain.accommodation.repository;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

  List<Accommodation> findByCategory(String category);

  @Query("SELECT a " +
      "FROM Accommodation a " +
      "JOIN a.products p " +
      "JOIN p.productInfoPerNightsList pin " +
      "WHERE a.category = :category " +
      "AND pin.date >= :checkIn AND pin.date < :checkOut " +
      "AND pin.count > 0 " +
      "AND p.maximumNumber >= :guestCount")
  List<Accommodation> findAvailableAccommodations(
      @Param("category") String category,
      @Param("checkIn") LocalDate checkIn,
      @Param("checkOut") LocalDate checkOut,
      @Param("guestCount") int guestCount);

}
