package com.travel.domain.product.service;

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
import java.time.temporal.ChronoUnit;
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
        Long accommodationId, LocalDate checkIn, LocalDate checkOut, int personNumber
    ) {

        if (checkIn == null) {
            checkIn = LocalDate.now();
        }

        if (checkOut == null) {
            checkOut = LocalDate.now().plusDays(1);
        }

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

            if (allDatesExist && personNumber <= product.getMaximumNumber()) {
                validProductList.add(product);
            }
        }

        if (validProductList.isEmpty()) {
            throw new ProductException(ErrorType.EMPTY_PRODUCT);
        }

        LocalDate finalCheckIn = checkIn;
        LocalDate finalCheckOut = checkOut;
        List<ProductResponse> productResponses = validProductList.stream()
            .map(product -> {
                ProductImageResponse productImageResponse = ProductImageResponse.from(product.getProductImage());
                int minCount = productInfoPerNightRepository.findMinCountByProductIdAndDateRange(
                    product.getId(), finalCheckIn, finalCheckOut);
                return ProductResponse.from(product, minCount, productImageResponse);
            })
            .collect(Collectors.toList());

        AccommodationImageResponse accommodationImageResponse = AccommodationImageResponse.from(accommodationEntity.getImages());
        AccommodationOptionResponse accommodationOptionResponse = AccommodationOptionResponse.from(accommodationEntity.getOptions());

        return AccommodationDetailListResponse.from(accommodationEntity, checkIn.toString(), checkOut.toString(), accommodationImageResponse, accommodationOptionResponse, productResponses);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(
        Long accommodationId, Long productId, LocalDate checkIn, LocalDate checkOut, int personNumber
    ) {
        if (checkIn == null) {
            checkIn = LocalDate.now();
        }

        if (checkOut == null) {
            checkOut = LocalDate.now().plusDays(1);
        }

        var accommodationEntity = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.EMPTY_ACCOMMODATION));

        var productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorType.EMPTY_PRODUCT));

        if (personNumber > productEntity.getMaximumNumber()) {
            throw new ProductException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
            if (!productInfoPerNightRepository.existsByProductIdAndDate(productId, date)) {
                throw new ProductException(ErrorType.EMPTY_PRODUCT);
            }
        }

        ProductInfoPerNight availableProductPerNight = productInfoPerNightRepository.findByProductIdAndDateRange(productId, checkIn, checkOut).get(0);
        int night = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
        int totalPrice = night * availableProductPerNight.getPrice();
        ProductImageResponse productImageResponse = ProductImageResponse.from(productEntity.getProductImage());
        ProductOptionResponse productOptionResponse = ProductOptionResponse.from(productEntity.getProductOption());

        return ProductDetailResponse.from(productEntity, accommodationEntity.getName(), availableProductPerNight.getPrice(), totalPrice, night, productImageResponse, productOptionResponse);
    }

}