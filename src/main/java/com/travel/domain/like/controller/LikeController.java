package com.travel.domain.like.controller;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.like.dto.request.LikeRequest;
import com.travel.domain.like.dto.response.LikeResponse;
import com.travel.domain.like.service.LikeService;
import com.travel.global.annotation.TokenUserId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public LikeResponse clickLike(@TokenUserId Long userId, @RequestBody LikeRequest likeRequest) {
        return likeService.clickLike(userId, likeRequest);
    }

    @GetMapping
    public ResponseEntity<List<AccommodationResponse>> getMyLikedAccommodations(
        @TokenUserId Long userId) {
        List<AccommodationResponse> likedAccommodations = likeService.getLikedAccommodations(userId);
        return ResponseEntity.ok(likedAccommodations);
    }
}
