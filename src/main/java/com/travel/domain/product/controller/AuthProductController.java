package com.travel.domain.product.controller;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.product.service.ProductService;
import com.travel.global.annotation.TokenUserId;
import com.travel.global.util.DateValidationUtil;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/accommodations/{accommodationId}")
public class AuthProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<AccommodationDetailListResponse> getAccommodationDetailByAuth(
        @TokenUserId Long tokenUserId,
        @PathVariable Long accommodationId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber
    ) {
        checkInDate = DateValidationUtil.checkInDate(checkInDate);
        checkOutDate = DateValidationUtil.checkOutDate(checkInDate, checkOutDate);

        var response = productService
            .getAccommodationDetailByAuth(accommodationId, checkInDate, checkOutDate, personNumber, tokenUserId);

        return ResponseEntity.ok(response);
    }

}
