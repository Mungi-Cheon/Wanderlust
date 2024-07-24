package com.travel.domain.accommodation.controller;

import com.travel.domain.accommodation.category.Category;
import com.travel.domain.accommodation.dto.request.AccommodationRequest;
import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.service.AccommodationService;
import com.travel.global.util.DateValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accommodation API", description = "숙소 API")
@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @Operation(summary = "숙소 조회", description = "조건에 맞게 숙소를 조회합니다.")
    @ApiResponse(content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = AccommodationResponse.class)))
    @GetMapping
    public ResponseEntity<List<AccommodationResponse>> getAvailableAccommodations(
        @Valid @ModelAttribute AccommodationRequest request) {

        String category = Category.fromId(request.getCategoryId());

        List<AccommodationResponse> responses = accommodationService
            .getAvailableAccommodations(category, request.getCheckInDate(),
                request.getCheckOutDate(), request.getPersonNumber());

        return ResponseEntity.ok(responses);
    }
}
