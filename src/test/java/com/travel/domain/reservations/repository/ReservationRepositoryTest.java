package com.travel.domain.reservations.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;
    private Accommodation accommodation;
    private Product product;

    private ProductImage productImage;

    private ProductInfoPerNight productInfoPerNight;

    @BeforeEach
    public void setUp() {
        accommodation = createAccommodation();
        product = createProduct();
//        productImage = createProductImage();
//        productInfoPerNight = createProductInfoPerNight();
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
    @DisplayName(value = "")
    void findAlreadyReservation() {
        User user = createUser("test101@gmail.com");

        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(2);
        int personNumber = 2;

        Reservation reservation = createReservation(user, checkInDate, checkOutDate, personNumber);
        Reservation savedReservation = reservationRepository.save(reservation);

        Optional<Reservation> result = reservationRepository
            .findAlreadyReservation(user.getId(), product.getId(), checkInDate, checkOutDate);

        if (result.isEmpty()) {
            assertEquals(Optional.empty(), result);
        } else {
            assertEquals(savedReservation.getId(), result.get().getId());
        }
    }

    private User createUser(String email) {
        User testUser = User.builder()
            .email(email)
            .username("testUser")
            .password("testUser1@#$")
            .build();
        return userRepository.save(testUser);
    }

    private Product createProduct() {
        Product testProduct = Product.builder()
            .accommodation(accommodation)
            .checkInTime("15:00")
            .checkOutTime("10:00")
            .description("description description")
            .type("Standard")
            .name("테스트 룸")
            .standardNumber(2)
            .maximumNumber(4)
            .build();

        return productRepository.save(testProduct);
    }

    private Accommodation createAccommodation() {
        Accommodation testAccommodation = Accommodation.builder()
            .name("숙소")
            .category("호텔")
            .description("테스트 호텔입니다")
            .address("테스트도 테스트시 테스트구 123-1")
            .grade(BigDecimal.valueOf(5.0))
            .contact("0503-1111-1111")
            .build();
        return accommodationRepository.save(testAccommodation);
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
            .personNumber(personNumber)
            .price(price)
            .night(night)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .user(user)
            .accommodation(accommodation)
            .product(product)
            .build();
    }
}