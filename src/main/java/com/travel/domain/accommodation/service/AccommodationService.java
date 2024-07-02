package com.travel.domain.accommodation.service;

import static com.travel.global.util.DateValidationUtil.isCheckInValid;
import static com.travel.global.util.DateValidationUtil.isCheckOutValid;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.type.ErrorType;
import com.travel.global.util.DateValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final ProductRepository productRepository;
    private final AccommodationRepository accommodationRepository;
    private final ProductInfoPerNightRepository productInfoPerNightRepository;

    @Transactional(readOnly = true)
    public Page<AccommodationResponse> getAvailableAccommodations(String category,
        LocalDate checkIn, LocalDate checkOut, int personNumber, Pageable pageable) {
        validateInputs(checkIn, checkOut, personNumber);

        List<Accommodation> accommodations;
        if (category == null) {
            accommodations = accommodationRepository.findAll();
        } else {
            accommodations = accommodationRepository.findByCategoryWithImagesAndOptions(category);
        }

        List<Accommodation> validAccommodationList = accommodations.stream()
            .filter(accommodation ->
                hasValidProducts(accommodation, checkIn, checkOut, personNumber))
            .collect(Collectors.toList());

        if (validAccommodationList.isEmpty()) {
            throw new AccommodationException(ErrorType.NOT_FOUND);
        }

        int start = Math.min((int) pageable.getOffset(), validAccommodationList.size());
        int end = Math.min((start + pageable.getPageSize()), validAccommodationList.size());
        List<AccommodationResponse> content = validAccommodationList.subList(start, end).stream()
            .map(AccommodationResponse::createAccommodationResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, validAccommodationList.size());
    }

    private void validateInputs(LocalDate checkIn, LocalDate checkOut, int personNumber) {
        if (!isCheckInValid(checkIn)) {
            throw new AccommodationException(ErrorType.INVALID_CHECK_IN);
        }
        if (!isCheckOutValid(checkIn, checkOut)) {
            throw new AccommodationException(ErrorType.INVALID_CHECK_OUT);
        }
        if (personNumber < 1) {
            throw new AccommodationException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }
    }

    private boolean hasValidProducts(Accommodation accommodation, LocalDate checkIn,
        LocalDate checkOut, Integer personNumber) {
        List<Product> productEntityList = productRepository.findAllByAccommodationId(
            accommodation.getId());
        return productEntityList.stream()
            .filter(product
                -> product.getStandardNumber() <= personNumber
                && product.getMaximumNumber() >= personNumber)
            .anyMatch(product -> areAllDatesAvailable(product.getId(), checkIn, checkOut));
    }

    private boolean areAllDatesAvailable(Long productId, LocalDate checkIn, LocalDate checkOut) {
        return checkIn.datesUntil(checkOut)
            .allMatch(
                date -> productInfoPerNightRepository.existsByProductIdAndDate(productId, date));
    }
}