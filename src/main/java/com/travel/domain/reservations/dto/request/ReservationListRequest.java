package com.travel.domain.reservations.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationListRequest {

    List<ReservationRequest> requestList;
}
