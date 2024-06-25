package com.travel.domain.accommodation.controller;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.service.AccommodationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    public ResponseEntity<List<AccommodationResponse>> getAvailableAccommodations(
        @RequestParam(defaultValue = "호텔") String category,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber) {

        if (checkInDate == null) {
            checkInDate = LocalDate.now();
        }

        if (checkOutDate == null) {
            checkOutDate = checkInDate.plusDays(1);
        }

        List<AccommodationResponse> responses = accommodationService
            .getAvailableAccommodations(category, checkInDate, checkOutDate, personNumber);
        return ResponseEntity.ok(responses);
    }
}
