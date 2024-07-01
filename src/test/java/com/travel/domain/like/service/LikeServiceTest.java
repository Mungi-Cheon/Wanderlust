package com.travel.domain.like.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.like.dto.request.LikeRequest;
import com.travel.domain.like.dto.response.LikeResponse;
import com.travel.domain.like.entity.Like;
import com.travel.domain.like.repository.LikeRepository;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @InjectMocks
    private LikeService likeService;

    private User user;

    private Accommodation accommodation;

    private Like like;

    @BeforeEach
    public void setUp() {
        user = User.builder()
            .id(1L)
            .build();

        accommodation = Accommodation.builder()
            .id(1L)
            .build();

        like = Like.builder()
            .user(user)
            .accommodation(accommodation)
            .build();
    }

    @Test
    @DisplayName("좋아요 클릭 성공")
    void testClickLike_success() {
        // given
        Long userId = 1L;
        LikeRequest request = new LikeRequest(1L);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(any())).thenReturn(Optional.of(accommodation));
        when(likeRepository.findByUserAndAccommodation(any(), any())).thenReturn(Optional.empty());
        when(likeRepository.countByAccommodation(any())).thenReturn(1);

        // when
        LikeResponse response = likeService.clickLike(userId, request);

        // then
        verify(likeRepository, times(1)).save(any(Like.class));
        assertNotNull(response);
        assertEquals(true, response.getLiked());
        assertEquals(1, response.getLikeCount());
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void testClickLike_cancelSuccess() {
        // given
        Long userId = 1L;
        LikeRequest request = new LikeRequest(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accommodationRepository.findById(request.getAccommodationId())).thenReturn(Optional.of(accommodation));
        when(likeRepository.findByUserAndAccommodation(user, accommodation)).thenReturn(Optional.of(like));
        when(likeRepository.countByAccommodation(accommodation)).thenReturn(0);

        // when
        LikeResponse response = likeService.clickLike(userId, request);

        // then
        verify(likeRepository, times(1)).delete(any(Like.class));
        assertNotNull(response);
        assertEquals(false, response.getLiked());
        assertEquals(0, response.getLikeCount());
    }
}