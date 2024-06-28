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

    List<Accommodation> findByCategory(String category);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Accommodation a WHERE a.id = :id")
    Optional<Accommodation> findByIdWithPessimisticLock(@Param("id") Long id);
}
