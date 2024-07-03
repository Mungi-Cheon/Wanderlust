package com.travel.domain.accommodation.controller;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.service.AccommodationService;
import com.travel.global.util.DateValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    public ResponseEntity<Page<AccommodationResponse>> getAvailableAccommodations(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "8") int size)  {

        checkInDate = DateValidationUtil.checkInDate(checkInDate);
        checkOutDate = DateValidationUtil.checkOutDate(checkInDate, checkOutDate);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,"id"));

        Page<AccommodationResponse> responses = accommodationService
            .getAvailableAccommodations(category, checkInDate, checkOutDate, personNumber, pageable)
            ;
        return ResponseEntity.ok(responses);
    }
}