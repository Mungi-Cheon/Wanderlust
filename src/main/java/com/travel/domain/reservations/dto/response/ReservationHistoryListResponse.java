package com.travel.domain.reservations.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ReservationHistoryListResponse {

    private List<ReservationHistoryResponse> reservationHistoryList;
}
