package com.travel.domain.reservations.repository;

import com.travel.domain.reservations.entity.Reservation;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r "
        + "FROM Reservation r "
        + "WHERE r.user.id = :userId "
        + "AND r.product.id = :productId "
        + "AND :checkInDate <= r.checkInDate "
        + "AND :checkOutDate >= r.checkOutDate")
    List<Reservation> findAlreadyReservation(
        @Param("userId") Long userId,
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);
}
