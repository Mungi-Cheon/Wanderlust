package com.travel.domain.product.controller;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.dto.response.ProductSimpleResponse;
import com.travel.domain.product.service.ProductService;
import com.travel.global.util.DateValidationUtil;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accommodations/{accommodationId}")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<AccommodationDetailListResponse> getAccommodationDetail(
        @PathVariable Long accommodationId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber
    ) {
        checkInDate = DateValidationUtil.checkInDate(checkInDate);
        checkOutDate = DateValidationUtil.checkOutDate(checkInDate, checkOutDate);

        var response = productService
            .getAccommodationDetail(accommodationId, checkInDate, checkOutDate, personNumber);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
        @PathVariable Long accommodationId,
        @PathVariable Long productId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") Integer personNumber
    ) {
        checkInDate = DateValidationUtil.checkInDate(checkInDate);
        checkOutDate = DateValidationUtil.checkOutDate(checkInDate, checkOutDate);

        var response = productService.getProductDetail(accommodationId, productId,
            checkInDate, checkOutDate, personNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductSimpleResponse>> getSearchProduct(
        @PathVariable Long accommodationId,
        @RequestParam String keyword
    ) {
        var response = productService.getSearchProduct(accommodationId, keyword);
        return ResponseEntity.ok(response);
    }

}