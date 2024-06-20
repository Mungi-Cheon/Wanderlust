package com.travel.domain.product.service;

import com.travel.domain.product.dto.request.AccommodationRequest;
import com.travel.domain.product.dto.response.*;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.repository.ProductImageRepository;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductOptionRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.type.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductInfoPerNightRepository productInfoPerNightRepository;
    private final ProductImageRepository productImageRepository;
    //private final AccomodationRepository accomodationRepository;

    @Transactional(readOnly = true)
    public AccommodationDetailListResponse getAccommodationDetail(
        Long accommodationId, AccommodationRequest request
    ) {//TODO acc repo
        var accomodationEntity = productRepository.findByAccommodationId(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.EMPTY_ACCOMMODATION)).getAccommodation();

        List<Product> productEntity = productRepository.findAllByAccommodationId(accommodationId);

        //인원, 날짜
        for (Product p : productEntity) {
            if (request.getPersonnel() <= p.getMaximumNumber()) {
                productEntity.add(p);
            } else if (request.isValidPeriod()) {
                productEntity.add(p);
            }
        }
        productEntity.stream()
            .map(ProductResponse::toResponse)
            .collect(Collectors.toList());

        /*ProductResponse.builder()
            .name(productEntity.get(0).toString())
            .checkOutTime(request.getCheckOut().toString())
            .checkOutTime(request.getCheckIn().toString())
            .pricePerNight()
            .standardNumber()
            .maximumNumber()
            .productOption()
            .pricePerNight()
            .build();
*/
        List<AccommodationImageResponse> accommodationImageResponses = accomodationEntity.getImages().stream()
            .map(AccommodationImageResponse::toResponse)
            .collect(Collectors.toList());


        return AccommodationDetailListResponse.builder()
            .id(accommodationId)
            .name(accomodationEntity.getName())
            .description(accomodationEntity.getDescription())
            .checkIn(request.getCheckIn().toString())
            .checkOut(request.getCheckOut().toString())
            .accommodationImageList(accommodationImageResponses)
            .productResponseList(productEntity.stream().map(
                ProductResponse::toResponse
            ).collect(Collectors.toList()))
            .build();

    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(
        Long accommodationId, String roomType, Long productId, AccommodationRequest request
    ) {//TODO acc repo 로 교체
        var accomodationEntity = productRepository.findByAccommodationId(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.EMPTY_ACCOMMODATION));

        //type, id 에 맞는 entity
        var productEntity = productRepository.findByAccommodationIdAndType(accomodationEntity.getId(), accomodationEntity.getType())
            .orElseThrow(() -> new AccommodationException(ErrorType.BAD_REQUEST));

        //인원 초과시 exception
        if (request.getPersonnel() > productEntity.getMaximumNumber()) {
            throw new AccommodationException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        //checkin, out 통해 1박당 정보 조회
        var availableProduct = productInfoPerNightRepository.findByProductIdAndDateRange(productId, request.getCheckIn(), request.getCheckOut())
            .orElseThrow(() -> new AccommodationException(ErrorType.BAD_REQUEST));

        var minPrice = productInfoPerNightRepository.findMinPriceByProductIdAndDateRange(productId, request.getCheckIn(), request.getCheckOut());

        var total = productInfoPerNightRepository.findTotalPriceByProductIdAndDateRange(productId, request.getCheckIn(), request.getCheckOut());

        ProductImageResponse productImageResponse = ProductImageResponse.toResponse(productEntity.getProductImage());

        var totalStay = productInfoPerNightRepository.findByDateBetweenAndProduct(request.getCheckIn(), request.getCheckOut().minusDays(1), productId);

        return ProductDetailResponse.builder()
            .id(productId)
            .name(productEntity.getName())
            .accommodationName(accomodationEntity.getName())
            .checkInTime(productEntity.getCheckInTime())
            .checkOutTime(productEntity.getCheckOutTime())
            .description(productEntity.getDescription())
            .pricePerNight(String.valueOf(minPrice)) //최저가
            .totalPrice(String.valueOf(total))// 총 가격
            .numberOfStay(totalStay) //숙박일
            .standardNumber(productEntity.getStandardNumber())
            .maximumNumber(productEntity.getMaximumNumber())
            .type(productEntity.getType())
            .productOption(productEntity.getProductOption())
            .productImageResponse(productImageResponse)
            .build();
    }

}
