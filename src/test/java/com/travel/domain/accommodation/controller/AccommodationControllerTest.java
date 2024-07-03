package com.travel.domain.accommodation.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class AccommodationControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AccommodationService accommodationService;
//
//    private AccommodationResponse accommodationResponse;
//
//    @Test
//    @WithMockUser
//    @DisplayName("숙소 조회 api 테스트")
//    void getAvailableAccommodations_Success() throws Exception {
//        accommodationResponse = AccommodationResponse.builder()
//            .id(1L)
//            .name("Test Hotel")
//            .category("호텔")
//            .build();
//
//        List<AccommodationResponse> mockResponse = List.of(accommodationResponse);
//        Pageable pageable = PageRequest.of(0, 8);
//        Page<AccommodationResponse> mockPage = new PageImpl<>(mockResponse, pageable, mockResponse.size());
//
//        Mockito.when(accommodationService.getAvailableAccommodations(
//                anyString(), any(LocalDate.class),
//                any(LocalDate.class), anyInt(), any(Pageable.class)))
//            .thenReturn(mockPage);
//
//        mockMvc.perform(get("/api/accommodations")
//                .param("category", "호텔")
//                .param("checkInDate", "2023-07-03")
//                .param("checkOutDate", "2023-07-04")
//                .param("personNumber", "2")
//                .param("page", "0")
//                .param("size", "8")
//                .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.content", hasSize(1)))
//            .andExpect(jsonPath("$.content[0].id", is(1)))
//            .andExpect(jsonPath("$.content[0].name", is("Test Hotel")))
//            .andExpect(jsonPath("$.content[0].category", is("호텔")));
//    }
}
