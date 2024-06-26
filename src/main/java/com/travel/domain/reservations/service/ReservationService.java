package com.travel.domain.reservations.service;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
//import com.travel.domain.email.service.EmailService;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationHistoryResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.reservations.repository.ReservationRepository;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.ReservationsException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final AccommodationRepository accommodationRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
//    private final EmailService emailService;

    @Transactional(readOnly = true)
    public ReservationHistoryListResponse getReservationHistories(String email) {
        User user = findUser(email);

        List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
        if (reservations.isEmpty()) {
            return ReservationHistoryListResponse.builder()
                .reservationHistoryList(new ArrayList<>()).build();
        }

        List<ReservationHistoryResponse> rhList = createReservationHistoryList(reservations);
        return ReservationHistoryListResponse.builder()
            .reservationHistoryList(rhList)
            .build();
    }

    @Transactional
    public ReservationResponse createReservation(
        ReservationRequest request, String email) {
        LocalDate checkInDate = request.getCheckInDate();
        LocalDate checkOutDate = request.getCheckOutDate();
        int night = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        long productId = request.getProductId();

        User user = findUser(email);

        Accommodation accommodation = accommodationRepository.findById(request.getAccommodationId())
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));

        checkAlreadyReserved(user, productId, checkInDate, checkOutDate);

        Product product = accommodation.getProducts()
            .stream()
            .filter(p -> p.getId().equals(productId))
            .findAny()
            .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));

        List<ProductInfoPerNight> piList = product.getProductInfoPerNightsList();
        decreaseCountByOne(piList, checkInDate, checkOutDate);

        Reservation reservation = Reservation.builder()
            .user(user)
            .accommodation(accommodation)
            .product(product)
            .personNumber(request.getPersonNumber())
            .price(piList.get(0).getPrice())
            .night(night)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .build();

        Reservation savedReservation = reservationRepository.save(reservation);

//        emailService.sendReservationConfirmation(email, savedReservation);

        log.debug("Saved reservation: {}", LocalDateTime.now());
        return ReservationResponse.from(savedReservation);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).get();
    }

    private List<ReservationHistoryResponse> createReservationHistoryList(
        List<Reservation> reservations) {
        List<ReservationHistoryResponse> rhList = new ArrayList<>();

        reservations.forEach(reservation -> {
            Product product = reservation.getProduct();
            rhList.add(
                ReservationHistoryResponse.from(
                    reservation,
                    product.getAccommodation().getName(),
                    product.getType(),
                    product.getStandardNumber(),
                    product.getMaximumNumber(),
                    product.getProductImage().getImageUrl1()
                )
            );
        });
        return rhList;
    }

    // 인자 개행
    private void checkAlreadyReserved(
        User user, Long productId,
        LocalDate checkInDate, LocalDate checkOutDate) {
        Optional<Reservation> already = reservationRepository.findAlreadyReservation(
            user.getId(), productId, checkInDate, checkOutDate);

        if (already.isPresent()) {
            throw new ReservationsException(ErrorType.ALREADY_RESERVATION);
        }
    }

    private void decreaseCountByOne(
        List<ProductInfoPerNight> productInfoPerNightsList, LocalDate checkInDate,
        LocalDate checkOutDate) {
        for (ProductInfoPerNight pi : productInfoPerNightsList) {
            if (!isValidDate(checkInDate, checkOutDate.minusDays(1), pi.getDate())) {
                continue;
            }
            if (pi.getCount() <= 0) {
                throw new ReservationsException(ErrorType.INCLUDES_FULLY_BOOKED_PRODUCT);
            }
            pi.decreaseCountByOne();
        }
    }

    private boolean isValidDate(
        LocalDate checkInDate, LocalDate checkOutDate,
        LocalDate date) {
        return (date.isEqual(checkInDate) || date.isAfter(checkInDate)) &&
            (date.isEqual(checkOutDate) || date.isBefore(checkOutDate));
    }
}