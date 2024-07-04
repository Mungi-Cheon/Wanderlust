package com.travel.domain.reservations.service;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.email.service.EmailService;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationHistoryResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.reservations.repository.ReservationRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.ReservationsException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final AccommodationRepository accommodationRepository;

    private final ReservationRepository reservationRepository;

    private final ProductRepository productRepository;

    private final ProductInfoPerNightRepository productInfoPerNightRepository;

    private final MemberRepository memberRepository;

    private final EmailService emailService;

    @Transactional(readOnly = true)
    public ReservationHistoryListResponse getReservationHistories(Long memberId) {

        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        if (reservations.isEmpty()) {
            return ReservationHistoryListResponse.builder()
                .reservationHistoryList(new ArrayList<>()).build();
        }

        List<ReservationHistoryResponse> rhList = createReservationHistoryList(reservations);
        return ReservationHistoryListResponse.builder()
            .reservationHistoryList(rhList)
            .build();
    }

    @CacheEvict(value = "accommodations", allEntries = true)
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, Long memberId) {
        LocalDate checkInDate = request.getCheckInDate();
        LocalDate checkOutDate = request.getCheckOutDate();
        int night = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        long productId = request.getProductId();

        Member member = findMember(memberId);

        Accommodation accommodation = findAccommodation(request.getAccommodationId());
        checkAlreadyReserved(memberId, productId, checkInDate, checkOutDate);

        Product product = findProduct(productId);

        List<ProductInfoPerNight> piList = findProductInfoPerNightList(productId, checkInDate,
            checkOutDate.minusDays(1));
        decreaseCountByOne(piList);

        Reservation reservation = createReservation(member, accommodation,
            product, request.getPersonNumber(), piList.get(0).getPrice(),
            night, checkInDate, checkOutDate);

        Reservation savedReservation = reservationRepository.save(reservation);

        emailService.sendReservationConfirmation(member.getEmail(), savedReservation);

        log.debug("Saved reservation: {}", LocalDateTime.now());
        return ReservationResponse.from(savedReservation);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
            () -> new MemberException(ErrorType.NOT_FOUND)
        );
    }

    private List<ReservationHistoryResponse> createReservationHistoryList(
        List<Reservation> reservations) {
        List<ReservationHistoryResponse> rhList = new ArrayList<>();

        reservations.forEach(reservation -> {
            Product product = reservation.getProduct();
            rhList.add(
                ReservationHistoryResponse.from(
                    reservation,
                    reservation.getAccommodation().getName(),
                    product.getType(),
                    product.getStandardNumber(),
                    product.getMaximumNumber(),
                    product.getProductImage().getImageUrl1()
                )
            );
        });
        return rhList;
    }

    private void checkAlreadyReserved(
        Long memberId, Long productId,
        LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> already = reservationRepository.findAlreadyReservationWithPessimisticLock(
            memberId, productId, checkInDate, checkOutDate);

        if (!already.isEmpty()) {
            throw new ReservationsException(ErrorType.ALREADY_RESERVATION);
        }
    }

    private void decreaseCountByOne(
        List<ProductInfoPerNight> productInfoPerNightsList) {
        for (ProductInfoPerNight pi : productInfoPerNightsList) {
            if (pi.getCount() <= 0) {
                throw new ReservationsException(ErrorType.INCLUDES_FULLY_BOOKED_PRODUCT);
            }
            pi.decreaseCountByOne();
        }
    }

    private Accommodation findAccommodation(Long accommodationId) {
        return accommodationRepository.findByIdJoinImagesAndOptions(
                accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));
    }

    private Product findProduct(long productId) {
        return productRepository.findByIdJoinImagesAndOption(productId)
            .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));
    }

    private List<ProductInfoPerNight> findProductInfoPerNightList(long productId,
        LocalDate checkInDate, LocalDate checkOutDate) {
        return productInfoPerNightRepository
            .findByProductIdAndDateRangeWithPessimisticLock(productId, checkInDate,
                checkOutDate);
    }

    private Reservation createReservation(
        Member member, Accommodation accommodation,
        Product product, Integer personNumber,
        int price, int night,
        LocalDate checkInDate, LocalDate checkOutDate) {
        return Reservation.builder()
            .member(member)
            .accommodation(accommodation)
            .product(product)
            .personNumber(personNumber)
            .price(price)
            .night(night)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .build();
    }
}