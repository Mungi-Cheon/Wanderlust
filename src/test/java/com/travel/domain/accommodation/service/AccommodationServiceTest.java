package com.travel.domain.accommodation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.accommodation.entity.AccommodationSearch;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.accommodation.repository.AccommodationSearchRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.type.ErrorType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {

    @Mock(strictness = Mock.Strictness.LENIENT)
    private AccommodationRepository accommodationRepository;

    @Mock
    private AccommodationSearchRepository accommodationSearchRepository;

    @InjectMocks
    private AccommodationService accommodationService;

    private Accommodation accommodation;

    private Product product;

    private ProductInfoPerNight productInfoPerNight;

    private AccommodationImage accommodationImage;

    @BeforeEach
    public void setUp() {
        productInfoPerNight = ProductInfoPerNight.builder()
            .date(LocalDate.now())
            .price(100000)
            .count(1)
            .build();

        product = Product.builder()
            .id(1L)
            .type("Standard")
            .standardNumber(2)
            .maximumNumber(4)
            .productInfoPerNightsList(List.of(productInfoPerNight))
            .build();

        accommodationImage = AccommodationImage.builder()
            .thumbnail("thumbnail")
            .build();

        accommodation = Accommodation.builder()
            .id(1L)
            .name("Test Hotel")
            .category("호텔")
            .grade(BigDecimal.valueOf(5))
            .products(List.of(product))
            .images(accommodationImage)
            .build();
    }

//    @Test
//    @DisplayName("성공 사례")
//    public void testGetAvailableAccommodations_success() {
//        // given
//        String keyword = "test";
//        String category = "호텔";
//        LocalDate checkIn = LocalDate.now();
//        LocalDate checkOut = checkIn.plusDays(1);
//        int personNumber = 2;
//
//        when(accommodationSearchRepository.findAccommodationsByName(any()))
//            .thenReturn(List.of());
//        when(accommodationRepository.findAccommodationsByCategory(any()))
//            .thenReturn(List.of(accommodation));
//        when(accommodationRepository.findAccommodationsByIdList(anyList(), anyLong()))
//            .thenReturn(List.of(accommodation));
//
//        // when
//        List<AccommodationResponse> result = accommodationService.getAvailableAccommodations(
//            keyword, category, checkIn, checkOut, personNumber, null);
//
//        // then
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.size());
//        assertEquals(accommodation.getId(), result.get(0).getId());
//    }

    @Test
    @DisplayName("실패 사례 : 조건에 해당하는 숙소가 존재하지 않음")
    public void testGetAvailableAccommodations_noAccommodationsFound() {
        // given
        String keyword = "test";
        String category = "호텔";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);
        int personNumber = 2;

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> accommodationService
                .getAvailableAccommodations(keyword, category, checkIn, checkOut, personNumber, null));

        // then
        assertEquals(ErrorType.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("실패 사례 : 체크인 날짜가 올바르지 않음")
    void getAvailableAccommodations_InvalidCheckIn_ThrowsException() {
        // given
        String keyword = "test";
        String category = "호텔";
        LocalDate checkIn = LocalDate.now().minusDays(1);
        LocalDate checkOut = checkIn.plusDays(2);
        int personNumber = 2;

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> accommodationService
                .getAvailableAccommodations(keyword, category, checkIn, checkOut, personNumber, null));

        // then
        assertEquals(ErrorType.INVALID_CHECK_IN.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("실패 사례 : 체크아웃 날짜가 올바르지 않음")
    void getAvailableAccommodations_InvalidCheckOut_ThrowsException() {
        // given
        String keyword = "test";
        String category = "호텔";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.minusDays(2);
        int personNumber = 2;

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> accommodationService
                .getAvailableAccommodations(keyword, category, checkIn, checkOut, personNumber, null));

        // then
        assertEquals(ErrorType.INVALID_CHECK_OUT.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("실패 사례 : 인원이 올바르지 않음")
    void getAvailableAccommodations_InvalidPersonNumber_ThrowsException() {
        // given
        String keyword = "test";
        String category = "호텔";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);
        int personNumber = 0;

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> accommodationService
                .getAvailableAccommodations(keyword, category, checkIn, checkOut, personNumber, null));

        // then
        assertEquals(ErrorType.INVALID_NUMBER_OF_PEOPLE.getMessage(), exception.getMessage());
    }
}
