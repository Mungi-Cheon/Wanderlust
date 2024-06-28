package com.travel.domain.reservations.controller;

import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.service.ReservationService;
import com.travel.global.annotation.TokenUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/history")
    public ResponseEntity<ReservationHistoryListResponse> getReservationHistories(
        @TokenUserId Long tokenUserId) {
        ReservationHistoryListResponse response = reservationService.getReservationHistories(
            tokenUserId);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ReservationResponse> reservation(
        @TokenUserId Long tokenUserId, @Valid @RequestBody ReservationRequest reservationRequest) {
        System.out.println("tokenUserId = " + tokenUserId);
        ReservationResponse response = reservationService.createReservation(reservationRequest,
            tokenUserId);
        return ResponseEntity.ok(response);
    }
}
