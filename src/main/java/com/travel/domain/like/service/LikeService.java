package com.travel.domain.like.service;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.like.dto.request.LikeRequest;
import com.travel.domain.like.dto.response.LikeResponse;
import com.travel.domain.like.entity.Like;
import com.travel.domain.like.repository.LikeRepository;
import com.travel.domain.user.entity.User;
import com.travel.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;

    @Transactional
    public LikeResponse clickLike(Long userId, LikeRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Accommodation accommodation = accommodationRepository.findById(request.getAccommodationId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid accommodation ID"));

        Optional<Like> existingLike = likeRepository.findByUserAndAccommodation(user, accommodation);
        boolean liked;

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            liked = false;
        } else {
            Like newLike = Like.builder()
                .user(user)
                .accommodation(accommodation)
                .liked(true)
                .build();
            likeRepository.save(newLike);
            liked = true;
        }

        int likeCount = likeRepository.countByAccommodation(accommodation);
        return new LikeResponse(liked, likeCount);
    }

    @Transactional(readOnly = true)
    public List<AccommodationResponse> getLikedAccommodations(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        List<Accommodation> likedAccommodations = likeRepository.findByUser(user).stream()
            .map(Like::getAccommodation)
            .toList();
        return likedAccommodations.stream()
            .map(AccommodationResponse::createAccommodationResponse)
            .collect(Collectors.toList());
    }
}
