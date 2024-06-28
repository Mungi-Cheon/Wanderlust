package com.travel.domain.reservations.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationHistoryResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.service.ReservationService;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper mapper;

    private final LocalDate checkInDate = LocalDate.now();

    private final LocalDate checkOutDate = LocalDate.now().plusDays(2);
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "예약 내역 조회")
    void getReservationHistories() throws Exception {
        // Given
        User user = createUser();

        ReservationHistoryResponse reservationResponse = createHistoryResponse();
        ReservationHistoryListResponse listResponse = createHistoryListResponse(
            reservationResponse);

        when(reservationService.getReservationHistories(user.getId()))
            .thenReturn(listResponse);

        // When & Then
        mockMvc.perform(get("/api/reservation/history")
                .header("mock-token", user.getId()))
            .andExpect(status().isOk())
            .andDo(print());
        then(reservationService).should().getReservationHistories(user.getId());
    }

    @Test
    @DisplayName(value = "예약")
    void reservation() throws Exception {
        // Given
        User user = createUser();

        ReservationRequest reservationRequest = createReservationRequest();
        ReservationResponse response = createReservationResponse();

        when(reservationService.createReservation(any(ReservationRequest.class),
            eq(user.getId()))).thenReturn(response);

        String content = mapper.writeValueAsString(reservationRequest);

        // When & Then
        mockMvc.perform(post("/api/reservation")
                .header("mock-token", user.getId())
                .content(content)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(66L))
            .andExpect(jsonPath("$.person_number", is(2)))
            .andExpect(jsonPath("$.total_price", is(600000)))
            .andExpect(jsonPath("$.check_in_date").value(checkInDate.toString()))
            .andExpect(jsonPath("$.check_out_date").value(checkOutDate.toString()));
    }


    private User createUser() {
        return User
            .builder()
            .id(1L)
            .email("test@gmail.com")
            .username("testUser")
            .password("testUser1@#$")
            .build();
    }

    private ReservationHistoryResponse createHistoryResponse() {
        return ReservationHistoryResponse
            .builder()
            .id(99L)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .night(2)
            .personNumber(2)
            .accommodationName("테스트 호텔")
            .roomType("VIP")
            .standardNumber(2)
            .maximumNumber(4)
            .imageUrl("ImageUrl")
            .price(400000)
            .totalPrice(800000)
            .build();
    }

    private ReservationHistoryListResponse createHistoryListResponse(
        ReservationHistoryResponse reservationResponse) {
        return ReservationHistoryListResponse
            .builder()
            .reservationHistoryList(List.of(reservationResponse))
            .build();
    }

    private ReservationResponse createReservationResponse() {
        return ReservationResponse
            .builder()
            .id(66L)
            .personNumber(2)
            .totalPrice(600000)
            .price(300000)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .build();
    }

    private ReservationRequest createReservationRequest() {
        return new ReservationRequest(1L, 3L, checkInDate, checkOutDate, 2);
    }
}