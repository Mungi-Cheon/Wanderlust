package com.travel.domain.reservations.controller;

import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.service.ReservationService;
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
    public ResponseEntity<ReservationHistoryListResponse> getReservationHistories() {
        ReservationHistoryListResponse response = reservationService.getReservationHistories(
            "c.mungi7421@gmail.com");
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ReservationResponse> reservation(
        @Valid @RequestBody ReservationRequest reservationRequest) {
        ReservationResponse response = reservationService.createReservation(reservationRequest,
            "c.mungi7421@gmail.com");
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/history2")
//    public ResponseEntity<ReservationHistoryListResponse> getReservations(
//        Authentication authentication) {
//        ReservationHistoryListResponse response = reservationService.getReservationHistories(
//            authentication.getName());
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/reservation2")
//    public ResponseEntity<ReservationResponse> reservation(
//        @Valid @RequestBody ReservationRequest reservationRequest, Authentication authentication) {
//        ReservationResponse response = reservationService.saveReservation(reservationRequest,
//            authentication.getName());
//        return ResponseEntity.ok(response);
//    }
}
