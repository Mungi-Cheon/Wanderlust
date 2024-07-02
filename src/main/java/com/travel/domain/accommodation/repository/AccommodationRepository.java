package com.travel.domain.accommodation.repository;

import com.travel.domain.accommodation.entity.Accommodation;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    List<Accommodation> findAll();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Accommodation a WHERE a.id = :id")
    Optional<Accommodation> findByIdWithLock(@Param("id") Long id);

    @Query(value = "SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options",
        countQuery = "SELECT COUNT(a) FROM Accommodation a")
    List<Accommodation> findAllWithImagesAndOptions();

    @Query(value = "SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options " +
        "WHERE a.category = :category",
        countQuery = "SELECT COUNT(a) FROM Accommodation a WHERE a.category = :category")
    List<Accommodation> findByCategoryWithImagesAndOptions(@Param("category") String category);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options " +
        "WHERE a.id = :id")
    Optional<Accommodation> findByIdJoinAndImagesOptionsWithPessimisticLock(@Param("id") Long id);
}
