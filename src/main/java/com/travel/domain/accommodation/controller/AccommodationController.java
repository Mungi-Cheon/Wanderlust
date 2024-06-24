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
      @RequestParam(required = false) LocalDate checkIn,
      @RequestParam(required = false) LocalDate checkOut,
      @RequestParam(defaultValue = "1") int guestCount) {

    if (checkIn == null) {
      checkIn = LocalDate.now();
    }

    if (checkOut == null) {
      checkOut = checkIn.plusDays(1);
    }

    List<AccommodationResponse> responses = accommodationService.getAvailableAccommodations(category, checkIn, checkOut, guestCount);
    return ResponseEntity.ok(responses);
  }
}
