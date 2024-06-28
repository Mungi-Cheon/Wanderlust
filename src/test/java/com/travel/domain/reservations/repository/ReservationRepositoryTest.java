package com.travel.domain.reservations.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.user.entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest {

    @Mock
    private ReservationRepository reservationRepository;

    private Accommodation accommodation;

    private Product product;

    @BeforeEach
    public void setUp() {
        accommodation = createAccommodation();
        product = createProduct();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName(value = "예약하기")
    void save() {
        User user = createUser("test99@gmail.com");

        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(1);
        int personNumber = 2;

        Reservation reservation = createReservation(user, checkInDate, checkOutDate, personNumber);
        Reservation saved = reservationRepository.save(reservation);

        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName(value = "예약 내역 조회")
    void findByUserId() {
        User user = createUser("test100@gmail.com");

        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(1);
        int personNumber = 2;

        Reservation reservation = createReservation(user, checkInDate, checkOutDate, personNumber);
        Reservation savedReservation = reservationRepository.save(reservation);

        List<Reservation> result = reservationRepository.findByUserId(user.getId());

        assertNotNull(result);
        assertEquals(savedReservation.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName(value = "동일한 예약한 날짜와 예약 상품이 존재하는 경우")
    void findAlreadyReservation() {
        User user = createUser("test101@gmail.com");

        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(2);
        int personNumber = 2;

        Reservation reservation = createReservation(user, checkInDate, checkOutDate, personNumber);
        when(reservationRepository.save(any())).thenReturn(reservation);

        assertNotNull(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);
        when(reservationRepository.findAlreadyReservation(anyLong(), anyLong(), any(), any()))
            .thenReturn(List.of(savedReservation));

        List<Reservation> result = reservationRepository
            .findAlreadyReservation(user.getId(), product.getId(), checkInDate, checkOutDate);

        assertEquals(1, result.size());
    }

    private User createUser(String email) {
        return User.builder()
            .id(1L)
            .email(email)
            .username("testUser")
            .password("testUser1@#$")
            .build();
    }

    private Product createProduct() {
        return Product.builder()
            .id(30L)
            .accommodation(accommodation)
            .checkInTime("15:00")
            .checkOutTime("10:00")
            .description("description description")
            .type("Standard")
            .name("테스트 룸")
            .standardNumber(2)
            .maximumNumber(4)
            .build();
    }

    private Accommodation createAccommodation() {
        return Accommodation.builder()
            .name("숙소")
            .category("호텔")
            .description("테스트 호텔입니다")
            .address("테스트도 테스트시 테스트구 123-1")
            .grade(BigDecimal.valueOf(5.0))
            .contact("0503-1111-1111")
            .build();
    }

    private ProductImage createProductImage() {
        return ProductImage.builder()
            .accommodation(accommodation)
            .product(product)
            .imageUrl1("Image1")
            .imageUrl2("Image2")
            .build();
    }

    private ProductInfoPerNight createProductInfoPerNight() {
        return ProductInfoPerNight.builder()
            .accommodation(accommodation)
            .product(product)
            .count(1)
            .price(200000)
            .date(LocalDate.now())
            .build();
    }

    private Reservation createReservation(User user,
        LocalDate checkInDate, LocalDate checkOutDate,
        int personNumber) {
        int night = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        int price = 300000;

        return Reservation.builder()
            .price(price)
            .night(night)
            .personNumber(personNumber)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .user(user)
            .build();
    }
}