package com.travel.domain.product.controller;

import com.travel.domain.accommodation.dto.request.AccommodationRequest;
import com.travel.domain.product.dto.response.AccommodationDetailListResponse;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.service.ProductService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accommodations/{accommodation_id}")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<AccommodationDetailListResponse> getAccommodationDetail(
        @PathVariable Long accommodation_id,
        @RequestParam(required = false) LocalDate checkIn,
        @RequestParam(required = false) LocalDate checkOut,
        @RequestParam(defaultValue = "2") int personNumber
    ) {
        if (checkIn == null) {
            checkIn = LocalDate.now();
        }

        if (checkOut == null) {
            checkOut = LocalDate.now().plusDays(1);
        }

        AccommodationRequest request = new AccommodationRequest(checkIn, checkOut, personNumber);
        var response = productService.getAccommodationDetail(accommodation_id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
        @PathVariable Long accommodation_id,
        @PathVariable Long product_id,
        @RequestParam(required = false) LocalDate checkIn,
        @RequestParam(required = false) LocalDate checkOut,
        @RequestParam(defaultValue = "2") Integer personNumber
    ) {
        if (checkIn == null) {
            checkIn = LocalDate.now();
        }

        if (checkOut == null) {
            checkOut = LocalDate.now().plusDays(1);
        }
        AccommodationRequest request = new AccommodationRequest(checkIn, checkOut, personNumber);
        var response = productService.getProductDetail(accommodation_id, product_id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AccommodationDetailListResponse> getAvailableProducts(
        @PathVariable Long accommodation_id,
        @RequestBody @Valid AccommodationRequest requestDTO) {
        AccommodationDetailListResponse responses = productService.getAccommodationDetail(accommodation_id, requestDTO);
        return ResponseEntity.ok(responses);
    }

}