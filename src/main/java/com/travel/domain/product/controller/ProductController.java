package com.travel.domain.product.controller;

import com.travel.domain.accommodation.dto.request.AccommodationRequest;
import com.travel.domain.product.dto.response.AccommodationDetailListResponse;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accommodations/{accommodation_id}")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<AccommodationDetailListResponse> getAccommodationDetail(
        @PathVariable Long accommodation_id,
        @RequestParam(required = false) LocalDate checkIn,
        @RequestParam(required = false) LocalDate checkOut,
        @RequestParam(required = false) Integer personNumber
    ) {
        AccommodationRequest request = new AccommodationRequest(checkIn, checkOut, personNumber);
        var response = productService.getAccommodationDetail(accommodation_id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomType}/{product_id}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
        @PathVariable Long accommodation_id,
        @PathVariable Long product_id,
        @RequestParam(required = false) LocalDate checkIn,
        @RequestParam(required = false) LocalDate checkOut,
        @RequestParam(required = false) Integer personNumber
    ) {
        AccommodationRequest request = new AccommodationRequest(checkIn, checkOut, personNumber);
        var response = productService.getProductDetail(accommodation_id, product_id, request);
        return ResponseEntity.ok(response);
    }

}
