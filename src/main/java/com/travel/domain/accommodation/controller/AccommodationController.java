package com.travel.domain.accommodation.controller;

import com.travel.domain.accommodation.dto.request.AccommodationRequest;
import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

  private final AccommodationService accommodationService;

  @GetMapping
  public ResponseEntity<List<AccommodationResponse>> getAllAccommodationsByCategory(@RequestParam String category) {
    List<AccommodationResponse> responses = accommodationService.getAllAccommodationsByCategory(category);
    return ResponseEntity.ok(responses);
  }

  @PostMapping
  public ResponseEntity<List<AccommodationResponse>> getAvailableAccommodations(
      @RequestParam(defaultValue = "호텔") String category,
      @RequestBody @Valid AccommodationRequest requestDTO) {
    List<AccommodationResponse> responses = accommodationService.getAvailableAccommodations(category, requestDTO);
    return ResponseEntity.ok(responses);
  }
}
