package com.travel.domain.accommodation.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.travel.domain.accommodation.entity.Accommodation;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccommodationRepositoryTest {

    @Autowired
    private AccommodationRepository accommodationRepository;

    private Accommodation accommodation1;
    private Accommodation accommodation2;

    @BeforeEach
    void setUp() {
        accommodation1 = Accommodation.builder()
            .category("호텔")
            .build();
        accommodation2 = Accommodation.builder()
            .category("리조트")
            .build();

        accommodationRepository.save(accommodation1);
        accommodationRepository.save(accommodation2);
    }

    @Test
    @DisplayName("카테고리를 통한 숙소 데이터 찾기")
    void findByCategory_ShouldReturnAccommodations() {
        // When
        List<Accommodation> result = accommodationRepository.findAccommodationsByCategory("호텔");

        // Then
        assertFalse(result.isEmpty());
        assertEquals("호텔", result.get(0).getCategory());
    }

    @Test
    @DisplayName("ID로 숙소 데이터 찾기, 이미지 및 옵션 포함")
    void findByIdJoinAndImagesOptions_ShouldReturnAccommodationWithImagesAndOptions() {
        // When
        Optional<Accommodation> result = accommodationRepository.findByIdJoinImagesAndOptions(accommodation1.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(accommodation1.getId(), result.get().getId());
    }

    @Test
    @DisplayName("숙소 ID로 숙소 찾기")
    void findAccommodationById(){
        Long id = accommodation1.getId();
        // When
        Accommodation result = accommodationRepository.findAccommodationById(id);

        // Then
        assertEquals(id, result.getId());
        assertEquals("호텔", result.getCategory());
    }

    @Test
    @DisplayName("ID 리스트로 숙소 데이터 찾기")
    void findAccommodationsByIdList_ShouldReturnAccommodations() {
        // When
        List<Accommodation> result = accommodationRepository.findAccommodationsByIdList(List.of(accommodation1.getId(), accommodation2.getId()), null);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }
}
