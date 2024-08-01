package com.travel.domain.accommodation.service;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationSearch;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.accommodation.repository.AccommodationSearchRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.ReservationsException;
import com.travel.global.exception.type.ErrorType;
import com.travel.global.util.DateValidationUtil;
import com.travel.global.util.ElasticSearchUtil;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final ProductInfoPerNightRepository productInfoPerNightRepository;
    private final AccommodationSearchRepository accommodationSearchRepository;
    private final ElasticSearchUtil elasticSearchUtil;

    private static final int PAGE_SIZE = 8;

    @Cacheable(value = "accommodations", keyGenerator = "customKeyGenerator")
    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAvailableAccommodations( String keyword, String category,
        LocalDate checkIn, LocalDate checkOut, int personNumber, Long lastAccommodationId) {
        validateInputs(checkIn, checkOut, personNumber);

        List<Long> keywordIdList = getIdByKeyword(keyword);
        List<Long> categoryIdList = getIdByCategory(category);
        List<Long> commonIdList = getCommonId(keywordIdList, categoryIdList);

        List<Accommodation> accommodations = accommodationRepository.findByIdList(commonIdList,
            lastAccommodationId);

        List<AccommodationResponse> validAccommodations = accommodations.stream()
            .filter(accommodation -> hasValidProducts(accommodation, checkIn, checkOut, personNumber))
            .limit(PAGE_SIZE)
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

    private boolean hasValidProducts(Accommodation accommodation, LocalDate checkIn, LocalDate checkOut, Integer personNumber) {
        List<Product> productEntityList = accommodation.getProducts();

        return productEntityList.stream()
            .filter(product -> product.getMaximumNumber() >= personNumber)
            .anyMatch(product -> areAllDatesAvailable(product.getId(), checkIn, checkOut));
    }

    private boolean areAllDatesAvailable(Long productId, LocalDate checkIn, LocalDate checkOut) {
        List<ProductInfoPerNight> perNights = productInfoPerNightRepository
            .findByProductIdAndDateRange(productId, checkIn, checkOut);

        return perNights.stream()
            .allMatch(perNight -> perNight.getCount() > 0);
    }

    // 데이터베이스에서 Elasticsearch로 동기화
    @PostConstruct
    public void syncData() {
        List<Accommodation> accommodations = accommodationRepository.findAll();
        elasticSearchUtil.bulkIndexAccommodation(accommodations);
    }

    private List<Long> getIdByKeyword(String keyword) {
        List<Long> idList;
        if (keyword!=null) {
            List<AccommodationSearch> searches = accommodationSearchRepository.findAccommodationsByname(keyword);
            idList = searches.stream().map(AccommodationSearch::getId).toList();
        } else {
            idList = new ArrayList<>();
        }
        return idList;
    }

    private List<Long> getIdByCategory(String category) {
        List<Long> idList;
        List<Accommodation> accommodation = accommodationRepository.findAccommodationsByCategory(category);
        idList = accommodation.stream().map(Accommodation::getId).toList();

        return idList;
    }

    private List<Long> getCommonId(List<Long> keywordIdList, List<Long> categoryIdList) {
        List<Long> commonIds;
        if (keywordIdList.isEmpty() && categoryIdList.isEmpty()) {
            throw new AccommodationException(ErrorType.NOT_FOUND);
        } else if (keywordIdList.isEmpty()) {
            commonIds = categoryIdList;
        }else if (categoryIdList.isEmpty()) {
            commonIds = keywordIdList;
        }else {
            Set<Long> idSet = new HashSet<>(keywordIdList);
            commonIds = categoryIdList.stream()
                .filter(idSet::contains)
                .collect(Collectors.toList());
        }
        return commonIds;
    }
}
