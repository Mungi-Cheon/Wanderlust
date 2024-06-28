//package com.travel.domain.reservations.controller;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.travel.domain.reservations.dto.request.ReservationRequest;
//import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
//import com.travel.domain.reservations.dto.response.ReservationHistoryResponse;
//import com.travel.domain.reservations.dto.response.ReservationResponse;
//import com.travel.domain.reservations.service.ReservationService;
//import java.time.LocalDate;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(controllers = ReservationController.class)
//class ReservationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReservationService reservationService;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//
//    private static final String TEST_EMAIL_ADDRESS = "test@gmail.com";
//
//    @Test
//    @DisplayName(value = "예약 내역 조회")
//    @WithMockUser(username = TEST_EMAIL_ADDRESS)
//    void getReservationHistories() throws Exception {
//        // Given
//        LocalDate checkInDate = LocalDate.of(2024, 7, 1);
//        LocalDate checkOutDate = LocalDate.of(2024, 7, 3);
//
//        ReservationHistoryResponse reservationResponse = ReservationHistoryResponse
//            .builder()
//            .id(99L)
//            .checkInDate(checkInDate)
//            .checkOutDate(checkOutDate)
//            .night(2)
//            .personNumber(2)
//            .accommodationName("테스트 호텔")
//            .roomType("VIP")
//            .standardNumber(2)
//            .maximumNumber(4)
//            .imageUrl("ImageUrl")
//            .price(400000)
//            .totalPrice(800000)
//            .build();
//
//        ReservationHistoryListResponse listResponse = ReservationHistoryListResponse
//            .builder()
//            .reservationHistoryList(List.of(reservationResponse))
//            .build();
//
//        when(reservationService.getReservationHistories(TEST_EMAIL_ADDRESS))
//            .thenReturn(listResponse);
//
//        // When & Then
//        mockMvc.perform(get("/api/reservation/history")
//                .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.reservationHistoryList", hasSize(1)))
//            .andExpect(jsonPath("$.reservationHistoryList[0].id").value(99L))
//            .andExpect(jsonPath("$.reservationHistoryList[0].checkInDate").value("2024-07-01"))
//            .andExpect(jsonPath("$.reservationHistoryList[0].checkOutDate").value("2024-07-03"))
//            .andExpect(jsonPath("$.reservationHistoryList[0].night").value(2))
//            .andExpect(jsonPath("$.reservationHistoryList[0].personNumber").value(2))
//            .andExpect(jsonPath("$.reservationHistoryList[0].accommodationName").value("테스트 호텔"))
//            .andExpect(jsonPath("$.reservationHistoryList[0].roomType").value("VIP"))
//            .andExpect(jsonPath("$.reservationHistoryList[0].standardNumber").value(2))
//            .andExpect(jsonPath("$.reservationHistoryList[0].maximumNumber").value(4))
//            .andExpect(jsonPath("$.reservationHistoryList[0].imageUrl").value("ImageUrl"))
//            .andExpect(jsonPath("$.reservationHistoryList[0].price").value(400000))
//            .andExpect(jsonPath("$.reservationHistoryList[0].totalPrice").value(800000));
//    }
//
////    @Test
////    @DisplayName(value = "예약")
////    @WithMockUser(username = TEST_EMAIL_ADDRESS)
////    void reservation() throws Exception {
////        // Given
////        LocalDate checkInDate = LocalDate.of(2024, 7, 1);
////        LocalDate checkOutDate = LocalDate.of(2024, 7, 3);
////
////        ReservationRequest reservationRequest = new ReservationRequest(1L, 3L, checkInDate,
////            checkOutDate, 2);
////
////        ReservationResponse response = ReservationResponse.builder()
////            .id(66L)
////            .personNumber(2)
////            .totalPrice(600000)
////            .price(300000)
////            .checkInDate(checkInDate)
////            .checkOutDate(checkOutDate)
////            .build();
////
////        when(reservationService.saveReservation(any(ReservationRequest.class),
////            eq(TEST_EMAIL_ADDRESS))).thenReturn(response);
////
////        String content = mapper.writeValueAsString(reservationRequest);
////
////        // When & Then
////        mockMvc.perform(post("/api/reservation")
////                .content(content)
////                .with(csrf())
////                .contentType(MediaType.APPLICATION_JSON))
////            .andExpect(status().isOk())
////            .andExpect(jsonPath("$.id").value(66L))
////            .andExpect(jsonPath("$.personNumber", is(2)))
////            .andExpect(jsonPath("$.totalPrice", is(600000)))
////            .andExpect(jsonPath("$.checkInDate").value(checkInDate.toString()))
////            .andExpect(jsonPath("$.checkOutDate").value(checkOutDate.toString()));
////    }
//}