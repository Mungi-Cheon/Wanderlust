package com.travel.domain.product.service;

import static com.travel.global.util.DateValidationUtil.isCheckInValid;
import static com.travel.global.util.DateValidationUtil.isCheckOutValid;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.accommodation.dto.response.AccommodationImageResponse;
import com.travel.domain.accommodation.dto.response.AccommodationOptionResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.like.repository.LikeRepository;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.dto.response.ProductImageResponse;
import com.travel.domain.product.dto.response.ProductOptionResponse;
import com.travel.domain.product.dto.response.ProductResponse;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.type.ErrorType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public AccommodationDetailListResponse getAccommodationDetail(
        Long accommodationId, LocalDate checkInDate,
        LocalDate checkOutDate, int personNumber) {
        validateInputs(checkInDate, checkOutDate, personNumber);

        Accommodation accommodationEntity = findAccommodation(accommodationId);

        List<Product> productEntityList = productRepository.findAllByAccommodationId(
            accommodationId);

        List<ProductInfoPerNight> productInfoPerNightList = productInfoPerNightRepository
            .findByAccommodationIdAndDateRange(accommodationId, checkInDate, checkOutDate);

        List<Product> validProductList = findProductList(productEntityList, productInfoPerNightList,
            checkInDate, checkOutDate, personNumber);

        if (validProductList.isEmpty()) {
            throw new ProductException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        List<ProductResponse> productResponses = findProductResponse(validProductList, productInfoPerNightList);

        AccommodationImageResponse accommodationImageResponse = AccommodationImageResponse
            .from(accommodationEntity.getImages());

        AccommodationOptionResponse accommodationOptionResponse = AccommodationOptionResponse
            .from(accommodationEntity.getOptions());

        Boolean liked = false;
        int likeCount = likeRepository.countByAccommodation(accommodationEntity);

        return AccommodationDetailListResponse.from(accommodationEntity, checkInDate,
            checkOutDate, accommodationImageResponse, accommodationOptionResponse,
            productResponses, liked, likeCount);
    }

    @Transactional(readOnly = true)
    public AccommodationDetailListResponse getAccommodationDetailByAuth(
        Long accommodationId, LocalDate checkInDate,
        LocalDate checkOutDate, int personNumber, Long memberId) {
        validateInputs(checkInDate, checkOutDate, personNumber);

        Accommodation accommodationEntity = findAccommodation(accommodationId);

        List<Product> productEntityList = productRepository.findAllByAccommodationId(
            accommodationId);

        List<ProductInfoPerNight> productInfoPerNightList = productInfoPerNightRepository
            .findByAccommodationIdAndDateRange(accommodationId, checkInDate, checkOutDate);

        List<Product> validProductList = findProductList(productEntityList, productInfoPerNightList,
            checkInDate, checkOutDate, personNumber);

        if (validProductList.isEmpty()) {
            throw new ProductException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        List<ProductResponse> productResponses = findProductResponse(validProductList, productInfoPerNightList);

        AccommodationImageResponse accommodationImageResponse = AccommodationImageResponse
            .from(accommodationEntity.getImages());

        AccommodationOptionResponse accommodationOptionResponse = AccommodationOptionResponse
            .from(accommodationEntity.getOptions());

        Boolean liked = getLikedStatus(accommodationEntity, memberId);
        int likeCount = likeRepository.countByAccommodation(accommodationEntity);

        return AccommodationDetailListResponse.from(accommodationEntity, checkInDate,
            checkOutDate, accommodationImageResponse, accommodationOptionResponse,
            productResponses, liked, likeCount);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(
        Long accommodationId, Long productId,
        LocalDate checkInDate, LocalDate checkOutDate,
        int personNumber) {
        validateInputs(checkInDate, checkOutDate, personNumber);

        Accommodation accommodationEntity = findAccommodation(accommodationId);

        Product productEntity = productRepository.
            findByIdAndAccommodationId(productId, accommodationId)
            .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));

        if (personNumber > productEntity.getMaximumNumber()) {
            throw new ProductException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        List<ProductInfoPerNight> availableProductPerNights
            = findAvailableProductPerNight(productId, checkInDate, checkOutDate);

        int totalPrice = calculateTotalPrice(availableProductPerNights);

        long expectedNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (availableProductPerNights.size() < expectedNights) {
            throw new ProductException(ErrorType.NOT_FOUND);
        }

        ProductImageResponse productImageResponse = ProductImageResponse.from(
            productEntity.getProductImage());
        ProductOptionResponse productOptionResponse = ProductOptionResponse.from(
            productEntity.getProductOption());

        return ProductDetailResponse.from(productEntity, accommodationEntity.getName(),
            availableProductPerNights.get(0).getPrice(), totalPrice, (int) expectedNights,
            productImageResponse, productOptionResponse);
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

    private Accommodation findAccommodation(Long accommodationId) {
        return accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));
    }

    private List<Product> findProductList(List<Product> productList, List<ProductInfoPerNight> productInfoPerNightList,
        LocalDate checkInDate, LocalDate checkOutDate,
        int personNumber) {
        Map<Long, List<ProductInfoPerNight>> perNightMap = productInfoPerNightList.stream()
            .collect(Collectors.groupingBy(p -> p.getProduct().getId()));

        return productList.stream()
            .filter(product -> {
                List<ProductInfoPerNight> infoPerNights = perNightMap.get(product.getId());
                if (infoPerNights == null ||
                    infoPerNights.size() < ChronoUnit.DAYS.between(checkInDate, checkOutDate)) {
                    return false;
                }
                return personNumber <= product.getMaximumNumber();
            })
            .collect(Collectors.toList());
    }

    private List<ProductResponse> findProductResponse(List<Product> productList,
        List<ProductInfoPerNight> productInfoPerNightList) {
        Map<Long, List<ProductInfoPerNight>> perNightMap = productInfoPerNightList.stream()
            .collect(Collectors.groupingBy(p -> p.getProduct().getId()));

        return productList.stream()
            .map(product -> {
                ProductImageResponse productImageResponse = ProductImageResponse.from(product.getProductImage());
                int minCount = perNightMap.get(product.getId()).stream()
                    .mapToInt(ProductInfoPerNight::getCount)
                    .min()
                    .orElse(0);
                return ProductResponse.from(product, minCount, productImageResponse);
            })
            .collect(Collectors.toList());
    }

    private List<ProductInfoPerNight> findAvailableProductPerNight(Long productId, LocalDate checkInDate,
        LocalDate checkOutDate) {
        List<ProductInfoPerNight> infoPerNightList = productInfoPerNightRepository
            .findByProductIdAndDateRange(productId, checkInDate, checkOutDate);
        return infoPerNightList;
    }

    private int calculateTotalPrice(List<ProductInfoPerNight> productInfoPerNightList) {
        return productInfoPerNightList.stream()
            .mapToInt(ProductInfoPerNight::getPrice)
            .sum();
    }

    private Boolean getLikedStatus(Accommodation accommodation, Long userId) {
        if (userId != null) {
            Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(ErrorType.NONEXISTENT_MEMBER));
            return likeRepository.findByMemberAndAccommodation(member, accommodation).isPresent();
        }
        return false;
    }
}