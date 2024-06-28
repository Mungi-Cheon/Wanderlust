//package com.travel.domain.reservations.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.travel.domain.accommodation.entity.Accommodation;
//import com.travel.domain.accommodation.repository.AccommodationRepository;
//import com.travel.domain.email.service.EmailService;
//import com.travel.domain.product.entity.Product;
//import com.travel.domain.product.entity.ProductImage;
//import com.travel.domain.product.entity.ProductInfoPerNight;
//import com.travel.domain.product.repository.ProductRepository;
//import com.travel.domain.reservations.dto.request.ReservationRequest;
//import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
//import com.travel.domain.reservations.dto.response.ReservationResponse;
//import com.travel.domain.reservations.entity.Reservation;
//import com.travel.domain.reservations.repository.ReservationRepository;
//import com.travel.domain.user.entity.User;
//import com.travel.domain.user.repository.UserRepository;
//import com.travel.global.exception.ReservationsException;
//import com.travel.global.exception.type.ErrorType;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class ReservationServiceTest {
//
//    @InjectMocks
//    private ReservationService reservationService;
//
//    @Mock
//    private AccommodationRepository accommodationRepository;
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private ReservationRepository reservationRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private EmailService emailService;
//
//    private User user;
//
//    private Accommodation accommodation;
//
//    private Product product;
//
//    private ProductImage productImage;
//
//    private ProductInfoPerNight productInfoPerNight;
//
//    private Reservation reservation;
//
//    private LocalDate checkInDate;
//
//    private LocalDate checkOutDate;
//
//    private static final Integer PRICE = 200000;
//
//    @BeforeEach
//    void setUp() {
//        checkInDate = LocalDate.now();
//        checkOutDate = checkInDate.plusDays(2);
//        user = createUser("testuser@gmail.com");
//        productInfoPerNight = createProductInfoPerNight();
//        productImage = createProductImage();
//        product = createProduct();
//        accommodation = createAccommodation();
//        reservation = createReservation();
//    }
//
//    @Test
//    @DisplayName("예약 내역 조회")
//    void testGetReservationHistories_success() {
//
//        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));
//        when(reservationRepository.findByUserId(any())).thenReturn(List.of(reservation));
//
//        ReservationHistoryListResponse result = reservationService.getReservationHistories(
//            "testuser@gmail.com");
//
//        verify(userRepository).findByEmail(any());
//        verify(reservationRepository).findByUserId(user.getId());
//
//        assertNotNull(result);
//        assertEquals(1, result.getReservationHistoryList().size());
//        assertEquals(400000, result.getReservationHistoryList().get(0).getTotalPrice());
//    }
//
//    @Test
//    @DisplayName("예약 생성")
//    void testCreateReservation_success() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));
//        when(accommodationRepository.findById(any())).thenReturn(
//            Optional.ofNullable(accommodation));
//        when(reservationRepository.save(any())).thenReturn(reservation);
//
//        ReservationRequest req = new ReservationRequest(
//            accommodation.getId(), product.getId(), checkInDate, checkOutDate, 2);
//        ReservationResponse result = reservationService.saveReservation(user.getId(), req);
//
//        verify(userRepository).findByEmail(any());
//        verify(reservationRepository).save(any());
//
//        assertNotNull(result);
//        assertEquals(checkInDate, result.getCheckInDate());
//        assertEquals(checkOutDate, result.getCheckOutDate());
//        assertEquals(req.getPersonNumber(), result.getPersonNumber());
//        int night = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
//        int totalPrice = night * productInfoPerNight.getPrice();
//        assertEquals(totalPrice, result.getTotalPrice());
//    }
//
//    @Test
//    @DisplayName("예약 생성 시 이미 예약된 경우 예외 발생")
//    void createReservation_alreadyReserved() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));
//        when(accommodationRepository.findById(any()))
//            .thenReturn(Optional.ofNullable(accommodation));
//        when(reservationRepository.findAlreadyReservation(any(), any(), any(), any()))
//            .thenReturn(Optional.ofNullable(reservation));
//
//        ReservationRequest request = new ReservationRequest(
//            accommodation.getId(), product.getId(), checkInDate, checkOutDate, 2);
//
//        ReservationsException exception = assertThrows(ReservationsException.class,
//            () -> reservationService.saveReservation(user.getId(), request));
//
//        verify(userRepository).findByEmail(any());
//        verify(reservationRepository).findAlreadyReservation(any(), any(), any(), any());
//
//        assertEquals(ErrorType.ALREADY_RESERVATION.getStatusCode(), exception.getStatusCode());
//        assertEquals(ErrorType.ALREADY_RESERVATION.getMessage(), exception.getMessage());
//    }
//
//    private User createUser(String email) {
//        return User.builder()
//            .id(35L)
//            .email(email)
//            .username("testUser")
//            .password("testUser1@#$")
//            .build();
//    }
//
//    private Reservation createReservation() {
//        int night = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
//        return Reservation.builder()
//            .id(99L)
//            .user(user)
//            .accommodation(accommodation)
//            .product(product)
//            .checkInDate(checkInDate)
//            .checkOutDate(checkOutDate)
//            .personNumber(2)
//            .night(night)
//            .price(PRICE)
//            .build();
//    }
//
//    private Accommodation createAccommodation() {
//        return Accommodation.builder()
//            .id(99L)
//            .products(List.of(product))
//            .name("숙소")
//            .category("호텔")
//            .description("테스트 호텔입니다")
//            .address("테스트도 테스트시 테스트구 123-1")
//            .grade(BigDecimal.valueOf(5.0))
//            .contact("0503-1111-1111")
//            .build();
//    }
//
//    private Product createProduct() {
//        return Product.builder()
//            .id(50L)
////            .accommodation(accommodation)
//            .productImage(productImage)
//            .productInfoPerNightsList(List.of(productInfoPerNight))
//            .checkInTime("15:00")
//            .checkOutTime("10:00")
//            .description("description description")
//            .type("Standard")
//            .name("테스트 룸")
//            .standardNumber(2)
//            .maximumNumber(4)
//            .build();
//    }
//
//    private ProductImage createProductImage() {
//        return ProductImage.builder()
//            .id(30L)
//            .imageUrl1("Image1")
//            .imageUrl2("Image2")
//            .build();
//    }
//
//    private ProductInfoPerNight createProductInfoPerNight() {
//        return ProductInfoPerNight.builder()
//            .id(78L)
//            .count(1)
//            .price(PRICE)
//            .date(LocalDate.now())
//            .build();
//    }
//}