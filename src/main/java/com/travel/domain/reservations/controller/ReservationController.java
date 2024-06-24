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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public ResponseEntity<ReservationHistoryListResponse> getReservationHistories() {
        ReservationHistoryListResponse response = reservationService.getReservationHistories(
            "test@gmail.com");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservation")
    public ResponseEntity<ReservationResponse> reservation(
        @Valid @RequestBody ReservationRequest reservationRequest) {
        ReservationResponse response = reservationService.saveReservation(reservationRequest,
            "sneor4807@gmail.com");
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/reservations")
//    public ResponseEntity<ReservationHistoryListResponse> getReservations(
//        Authentication authentication) {
//        ReservationHistoryListResponse response = reservationService.getReservation(
//            authentication.getName());
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/reservation")
//    public ResponseEntity<ReservationResponse> reservation(
//        @Valid @RequestBody ReservationRequest reservationRequest, Authentication authentication) {
//        ReservationResponse response = reservationService.saveReservation(reservationRequest,
//            authentication.getName());
//        return ResponseEntity.ok(response);
//    }
}
