package com.travel.domain.product.service;

import static com.travel.global.util.DateValidationUtil.isCheckInValid;
import static com.travel.global.util.DateValidationUtil.isCheckOutValid;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.accommodation.dto.response.AccommodationImageResponse;
import com.travel.domain.accommodation.dto.response.AccommodationOptionResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
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
import java.util.Map;
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
        Long accommodationId, LocalDate checkInDate,
        LocalDate checkOutDate, int personNumber) {
        validateInputs(checkInDate, checkOutDate, personNumber);

        Accommodation accommodationEntity = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));

        List<Product> productEntityList = productRepository.findAllByAccommodationId(
            accommodationId);

        // 날짜 범위 내의 모든 ProductInfoPerNight 데이터를 한 번에 가져와 메모리에서 처리한다.
        // 전 버전: 각 제품에 대해 날짜별로 개별 쿼리를 실행하여 데이터베이스에서 확인
        //뭐지
        List<ProductInfoPerNight> productInfoPerNightList = productInfoPerNightRepository
            .findByAccommodationIdAndDateRange(accommodationId, checkInDate, checkOutDate);

        //유효 정보 필터링
        //요청 인원수 personNumber가 product.getMaximumNumber 보다 크면 false
        Map<Long, List<ProductInfoPerNight>> productInfoPerNightsMap = productInfoPerNightList.stream()
            .collect(Collectors.groupingBy(p -> p.getProduct().getId()));

        List<Product> validProductList = productEntityList.stream()
            .filter(product -> {
                List<ProductInfoPerNight> infoPerNights = productInfoPerNightsMap.get(product.getId());
                if (infoPerNights == null || infoPerNights.size() < ChronoUnit.DAYS.between(checkInDate, checkOutDate)) {
                    return false;
                }
                return personNumber <= product.getMaximumNumber();
            })
            .collect(Collectors.toList());

        if (validProductList.isEmpty()) {
            throw new ProductException(ErrorType.NOT_FOUND);
        }

        //유효 정보 응답 생성
        List<ProductResponse> productResponses = validProductList.stream()
            .map(product -> {
                ProductImageResponse productImageResponse = ProductImageResponse.from(product.getProductImage());
                int minCount = productInfoPerNightsMap.get(product.getId()).stream()
                    .mapToInt(ProductInfoPerNight::getCount)
                    .min()
                    .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));
                return ProductResponse.from(product, minCount, productImageResponse);
            })
            .collect(Collectors.toList());

        AccommodationImageResponse accommodationImageResponse = AccommodationImageResponse
            .from(accommodationEntity.getImages());

        AccommodationOptionResponse accommodationOptionResponse = AccommodationOptionResponse
            .from(accommodationEntity.getOptions());

        return AccommodationDetailListResponse.from(accommodationEntity, checkInDate,
            checkOutDate, accommodationImageResponse,
            accommodationOptionResponse, productResponses);

     /*  List<Product> validProductList = new ArrayList<>();
        for (Product product : productEntityList) {
            boolean allDatesExist = true;

            // select * from productInfoPerNight p where p.productId = id and between checkInDate and date and checkOutDate
            // 객실 1개당 6월~8월까지 데이터, -> 얘네들을 하나씩 비교검사를 하고 있어요
            for (LocalDate date = checkInDate; date.isBefore(checkOutDate);
                date = date.plusDays(1)) {
                if (!productInfoPerNightRepository
                    .existsByProductIdAndDate(product.getId(),
                        date)) {
                    allDatesExist = false;
                    break;
                }
            }

            if (allDatesExist && personNumber <= product.getMaximumNumber()) {
                validProductList.add(product);
            }
        }

        if (validProductList.isEmpty()) {
            throw new ProductException(ErrorType.NOT_FOUND);
        }

        List<ProductResponse> productResponses = validProductList.stream()
            .map(product -> {
                ProductImageResponse productImageResponse = ProductImageResponse.from(
                    product.getProductImage());
                int minCount = productInfoPerNightRepository
                    .findMinCountByProductIdAndDateRange(
                        product.getId(), checkInDate, checkOutDate);
                return ProductResponse.from(product, minCount, productImageResponse);
            })
            .collect(Collectors.toList());

        AccommodationImageResponse accommodationImageResponse = AccommodationImageResponse.from(
            accommodationEntity.getImages());
        AccommodationOptionResponse accommodationOptionResponse = AccommodationOptionResponse.from(
            accommodationEntity.getOptions());

        return AccommodationDetailListResponse.from(accommodationEntity, checkInDate,
            checkOutDate, accommodationImageResponse, accommodationOptionResponse,
            productResponses);*/
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(
        Long accommodationId, Long productId,
        LocalDate checkInDate, LocalDate checkOutDate,
        int personNumber) {
        validateInputs(checkInDate, checkOutDate, personNumber);

        Accommodation accommodationEntity = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));

        Product productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));

        if (personNumber > productEntity.getMaximumNumber()) {
            throw new ProductException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        List<ProductInfoPerNight> availableProductPerNights = productInfoPerNightRepository
            .findByProductIdAndDateRange(productId, checkInDate, checkOutDate);

        // 날짜 범위 내의 모든 날짜가 존재하는지 확인하고 메모리서 처리
        long expectedNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (availableProductPerNights.size() < expectedNights) {
            throw new ProductException(ErrorType.NOT_FOUND);
        }

        // 총 가격 계산
        int totalPrice = availableProductPerNights.stream()
            .mapToInt(ProductInfoPerNight::getPrice)
            .sum();

        ProductImageResponse productImageResponse = ProductImageResponse.from(
            productEntity.getProductImage());
        ProductOptionResponse productOptionResponse = ProductOptionResponse.from(
            productEntity.getProductOption());

        return ProductDetailResponse.from(productEntity, accommodationEntity.getName(),
            availableProductPerNights.get(0).getPrice(), totalPrice, (int) expectedNights,
            productImageResponse, productOptionResponse);

       /* for (LocalDate date = checkInDate; date.isBefore(checkOutDate); date = date.plusDays(1)) {
            if (!productInfoPerNightRepository.existsByProductIdAndDate(productId, date)) {
                throw new ProductException(ErrorType.NOT_FOUND);
            }
        }

        ProductInfoPerNight availableProductPerNight = productInfoPerNightRepository
            .findByProductIdAndDateRange(productId, checkInDate, checkOutDate).get(0);

        int night = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        int totalPrice = night * availableProductPerNight.getPrice();

        ProductImageResponse productImageResponse = ProductImageResponse.from(
            productEntity.getProductImage());
        ProductOptionResponse productOptionResponse = ProductOptionResponse.from(
            productEntity.getProductOption());

        return ProductDetailResponse.from(productEntity, accommodationEntity.getName(),
            availableProductPerNight.getPrice(), totalPrice, night, productImageResponse,
            productOptionResponse);*/
    }
    private void validateInputs(LocalDate checkInDate, LocalDate checkOutDate,
                                int personNumber) {
        if (!isCheckInValid(checkInDate)) {
            throw new AccommodationException(ErrorType.INVALID_CHECK_IN);
        }
        if (!isCheckOutValid(checkInDate, checkOutDate)) {
            throw new AccommodationException(ErrorType.INVALID_CHECK_OUT);
        }
        if (personNumber < 1) {
            throw new AccommodationException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }
    }
}