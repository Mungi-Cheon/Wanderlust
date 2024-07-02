//package com.travel.domain.accommodation.repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//import com.travel.domain.accommodation.entity.Accommodation;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class AccommodationRepositoryTest {
//
//    @Autowired
//    private AccommodationRepository accommodationRepository;
//
//    private Accommodation accommodation;
//
//    @Test
//    @DisplayName("카테고리를 통한 숙소 데이터 찾기")
//    void findByCategory_ShouldReturnAccommodations() {
//        // Given
//        accommodation = Accommodation.builder()
//            .category("호텔")
//            .build();
//
//        accommodationRepository.save(accommodation);
//
//        // When
//        List<Accommodation> result = accommodationRepository.findByCategory("호텔");
//
//        // Then
//        assertFalse(result.isEmpty());
//        assertEquals("호텔", result.get(0).getCategory());
//    }
//}