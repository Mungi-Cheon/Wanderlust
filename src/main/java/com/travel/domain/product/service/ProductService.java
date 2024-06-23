package com.travel.domain.product.service;

import com.travel.domain.accommodation.dto.request.AccommodationRequest;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.accommodation.dto.response.AccommodationImageResponse;
import com.travel.domain.accommodation.dto.response.AccommodationOptionResponse;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.dto.response.ProductImageResponse;
import com.travel.domain.product.dto.response.ProductOptionResponse;
import com.travel.domain.product.dto.response.ProductResponse;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductInfoPerNightRepository productInfoPerNightRepository;
    private final AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public AccommodationDetailListResponse getAccommodationDetail(
        Long accommodationId, AccommodationRequest request
    ) {
        LocalDate checkIn = request.getCheckIn();
        LocalDate checkOut = request.getCheckOut();

        var accommodationEntity = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.EMPTY_ACCOMMODATION));

        List<Product> productEntityList = productRepository.findAllByAccommodationId(accommodationId);

        List<Product> validProductList = new ArrayList<>();

        for (Product product : productEntityList) {
            boolean allDatesExist = true;

            for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
                if (!productInfoPerNightRepository.existsByProductIdAndDate(product.getId(), date)) {
                    allDatesExist = false;
                    break;
                }
            }

            if (allDatesExist && request.getGuestCount() <= product.getMaximumNumber()) {
                validProductList.add(product);
            }
        }

        if(validProductList.isEmpty()) {
            throw new ProductException(ErrorType.EMPTY_PRODUCT);
        }

        List<ProductResponse> productResponses = validProductList.stream()
            .map(product -> {
                ProductImageResponse productImageResponse = ProductImageResponse.from(product.getProductImage());
                int minCount = productInfoPerNightRepository.findMinCountByProductIdAndDateRange(
                    product.getId(), checkIn, checkOut);
                return ProductResponse.from(product, minCount, productImageResponse);
            })
            .collect(Collectors.toList());

        AccommodationImageResponse accommodationImageResponse = AccommodationImageResponse.from(accommodationEntity.getImages());
        AccommodationOptionResponse accommodationOptionResponse = AccommodationOptionResponse.from(accommodationEntity.getOptions());

        return AccommodationDetailListResponse.from(accommodationEntity, checkIn.toString(), checkOut.toString(), accommodationImageResponse, accommodationOptionResponse, productResponses);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(
        Long accommodationId, Long productId, AccommodationRequest request
    ) {

        var accommodationEntity = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.EMPTY_ACCOMMODATION));

        var productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorType.EMPTY_PRODUCT));

        if (request.getGuestCount() > productEntity.getMaximumNumber()) {
            throw new ProductException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        LocalDate checkIn = request.getCheckIn();
        LocalDate checkOut = request.getCheckOut();

        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
            if (!productInfoPerNightRepository.existsByProductIdAndDate(productId, date)) {
                throw new ProductException(ErrorType.EMPTY_PRODUCT);
            }
        }

        ProductInfoPerNight availableProductPerNight = productInfoPerNightRepository.findByProductIdAndDateRange(productId, request.getCheckIn(), request.getCheckOut()).get(0);
        var total = productInfoPerNightRepository.findTotalPriceByProductIdAndDateRange(productId, request.getCheckIn(), request.getCheckOut());
        var totalStay = productInfoPerNightRepository.findByDateBetweenAndProduct(request.getCheckIn(), request.getCheckOut().minusDays(1), productId);
        ProductImageResponse productImageResponse = ProductImageResponse.from(productEntity.getProductImage());
        ProductOptionResponse productOptionResponse = ProductOptionResponse.from(productEntity.getProductOption());

        return ProductDetailResponse.from(productEntity, accommodationEntity.getName(), availableProductPerNight.getPrice(), total, totalStay, productImageResponse, productOptionResponse);
    }

}