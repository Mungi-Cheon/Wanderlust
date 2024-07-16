package com.travel.domain.review.service;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.reservations.repository.ReservationRepository;
import com.travel.domain.review.dto.request.ReviewRequest;
import com.travel.domain.review.dto.response.AccommodationReviewResponseList;
import com.travel.domain.review.dto.response.DeleteReviewResponse;
import com.travel.domain.review.dto.response.ReviewResponse;
import com.travel.domain.review.dto.response.UpdateReviewResponse;
import com.travel.domain.review.entity.Review;
import com.travel.domain.review.repository.ReviewRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.ReservationsException;
import com.travel.global.exception.ReviewException;
import com.travel.global.exception.type.ErrorType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AccommodationRepository accommodationRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public AccommodationReviewResponseList getReviewList(Long accommodationId) {

        Accommodation accommodation = findAccommodation(accommodationId);
        List<Review> reviewList = accommodation.getReviews();

        List<ReviewResponse> reviewResponseList = reviewList.stream()
            .map(ReviewResponse::from)
            .collect(Collectors.toList());

        return AccommodationReviewResponseList.from(accommodation, reviewResponseList);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getMyReviewList(Long memberId) {
        Member member = findMember(memberId);
        List<Review> reviewList = member.getReviews();

        List<ReviewResponse> reviewResponseList = reviewList.stream()
            .map(ReviewResponse::from)
            .collect(Collectors.toList());

        return reviewResponseList;
    }

    @Transactional
    public ReviewResponse createReview(Long memberId, Long accommodationId,
        ReviewRequest reviewRequest) {
        Accommodation accommodation = findAccommodation(accommodationId);
        Member member = findMember(memberId);
        Reservation reservation = findReservation(reviewRequest.getReservationId());
        Long productId = reservation.getProduct().getId();
        productRepository.findByIdAndAccommodationId(productId, accommodationId)
            .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));
        //중복 리뷰 검증
        if (reviewRepository.existsByReservationId(reservation.getId())) {
            throw new ReviewException(ErrorType.DUPLICATED_REVIEW);
        }
        //체크아웃 시간, 작성 시간 검증
        LocalDate checkOutTime = reservation.getCheckOutDate();
        LocalDate createdAt = LocalDate.now();
        validInputs(checkOutTime, createdAt);

        Review review = Review.from(reviewRequest, accommodation, member, reservation);
        reviewRepository.save(review);
        return ReviewResponse.from(review);
    }

    @Transactional
    public UpdateReviewResponse updateReview(Long memberId, Long accommodationId,
        ReviewRequest reviewRequest, Long reviewId) {
        Accommodation accommodaion = findAccommodation(accommodationId);
        findMember(memberId);
        Reservation reservation = findReservation(reviewRequest.getReservationId());
        Review review = findByIdAndAccommodationId(reviewId, accommodationId);
        LocalDate checkOutTime = reservation.getCheckOutDate();
        LocalDate updatedAt = LocalDate.now();
        isValidWrite(checkOutTime, updatedAt);
        review.update(reviewRequest);

        Review reviewUpdated = reviewRepository.save(review);
        return UpdateReviewResponse.from(reviewUpdated);
    }

    @Transactional
    public DeleteReviewResponse deleteReview(Long memberId,
        Long accommodationId, Long reviewId) {
        findMember(memberId);
        Review review = findByIdAndAccommodationId(reviewId, accommodationId);
        reviewRepository.delete(review);

        return DeleteReviewResponse.from(review);

    }

    @Transactional
    public void updateGrade() {
        List<Accommodation> accommodations = accommodationRepository.findAll();
        for (Accommodation accommodation : accommodations) {
            List<Review> reviews = reviewRepository.getByAccommodationId(accommodation.getId());
            BigDecimal newGrade = calculateGrade(reviews, accommodation);
            accommodation.updateGrade(newGrade);
        }
        accommodationRepository.saveAll(accommodations);
    }

    //TODO product_v2 의 default method로 교체
    private Accommodation findAccommodation(Long accommodationId) {
        return accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));
    }

    private Member findMember(Long memberId) {
        return memberRepository.getMember(memberId);
    }

    private Reservation findReservation(Long reservationId) {
        return reservationRepository.getReservationById(reservationId);
    }

    private Review findByIdAndAccommodationId(Long reviewId, Long accommodationId) {
        return reviewRepository.getByIdAndAccommodationId(reviewId, accommodationId);
    }

    private static boolean isValidWrite(LocalDate checkOutDate, LocalDate createdAt) {
        return createdAt.isAfter(checkOutDate) || createdAt.isEqual(checkOutDate);
    }

    private void validInputs(LocalDate checkOutDate, LocalDate createdAt) {
        if (!isValidWrite(checkOutDate, createdAt)) {
            throw new ReviewException(ErrorType.FAILED_TO_CREATE_REVIEW);
        }
    }

    private BigDecimal calculateGrade(List<Review> reviews, Accommodation accommodation) {
        if (reviews.isEmpty()) {
            return accommodation.getGrade(); //return BigDecimal.ZERO;
        }

        BigDecimal totalGrade = reviews.stream()
            .map(Review::getGrade)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalGrade.divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
    }
}
