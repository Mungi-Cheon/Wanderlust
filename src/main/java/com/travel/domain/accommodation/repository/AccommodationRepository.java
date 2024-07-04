package com.travel.domain.accommodation.repository;

import com.travel.domain.accommodation.entity.Accommodation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query(value = "SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options")
    List<Accommodation> findAllAccommodations();

    @Query(value = "SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options " +
        "WHERE a.category = :category")
    List<Accommodation> findAllAccommodationsByCategory(@Param("category") String category);

    @Query("SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options " +
        "WHERE a.id = :id")
    Optional<Accommodation> findByIdJoinImagesAndOptions(@Param("id") Long id);
}