package com.travel.domain.reservations.repository;

import com.travel.domain.reservations.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    @Query("SELECT r "
        + "FROM Reservation r "
        + "WHERE r.user.id = :userId "
        + "AND r.product.id = :productId "
        + "AND :checkInDate <= r.checkInDate "
        + "AND :checkOutDate >= r.checkOutDate")
    Optional<Reservation> findAlreadyReservation(
        @Param("userId") Long userId,
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);
}
