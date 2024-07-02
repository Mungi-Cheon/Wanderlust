package com.travel.domain.reservations.controller;

import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.service.ReservationService;
import com.travel.global.annotation.TokenMemberId;
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
        @TokenMemberId Long tokenMemberId) {
        ReservationHistoryListResponse response = reservationService.getReservationHistories(
            tokenMemberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ReservationResponse> reservation(
        @TokenMemberId Long tokenMemberId, @Valid @RequestBody ReservationRequest reservationRequest) {
        System.out.println("tokenMemberId = " + tokenMemberId);
        ReservationResponse response = reservationService.createReservation(reservationRequest,
            tokenMemberId);
        return ResponseEntity.ok(response);
    }
}