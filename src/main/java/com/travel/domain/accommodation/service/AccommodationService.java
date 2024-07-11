package com.travel.domain.accommodation.service;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.type.ErrorType;
import com.travel.global.util.DateValidationUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final ProductRepository productRepository;
    private final AccommodationRepository accommodationRepository;
    private final ProductInfoPerNightRepository productInfoPerNightRepository;

    @Cacheable(value = "accommodations",
        key = "#category + '-' + #checkIn + '-' + "
            + "#checkOut + '-' + #personNumber")
    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAvailableAccommodations(
        String category, LocalDate checkIn, LocalDate checkOut, int personNumber) {
        validateInputs(checkIn, checkOut, personNumber);

        List<Accommodation> accommodations;
        if (category.isEmpty()) {
            accommodations = accommodationRepository.findAllAccommodations();
        } else {
            accommodations = accommodationRepository.findAllAccommodationsByCategory(category);
        }

        List<AccommodationResponse> validAccommodations = accommodations.stream()
            .filter(
                accommodation -> hasValidProducts(accommodation, checkIn, checkOut, personNumber))
            .map(AccommodationResponse::createAccommodationResponse)
            .collect(Collectors.toList());

        if (validAccommodations.isEmpty()) {
            throw new AccommodationException(ErrorType.NOT_FOUND);
        }

        return validAccommodations;
    }

    private void validateInputs(LocalDate checkIn, LocalDate checkOut, int personNumber) {
        if (!DateValidationUtil.isCheckInValid(checkIn)) {
            throw new AccommodationException(ErrorType.INVALID_CHECK_IN);
        }
        if (!DateValidationUtil.isCheckOutValid(checkIn, checkOut)) {
            throw new AccommodationException(ErrorType.INVALID_CHECK_OUT);
        }
        if (personNumber < 1) {
            throw new AccommodationException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }
    }

    private boolean hasValidProducts(Accommodation accommodation, LocalDate checkIn,
        LocalDate checkOut, Integer personNumber) {
        List<Product> productEntityList = productRepository
            .findAllByAccommodationIdWithFetchJoin(accommodation.getId());

        return productEntityList.stream()
            .filter(product -> product.getMaximumNumber() >= personNumber)
            .anyMatch(product -> areAllDatesAvailable(product.getId(), checkIn, checkOut));
    }

    private boolean areAllDatesAvailable(Long productId, LocalDate checkIn, LocalDate checkOut) {
        return checkIn.datesUntil(checkOut)
            .allMatch(date ->
                productInfoPerNightRepository.existsByProductIdAndDate(productId, date));
    }
}
