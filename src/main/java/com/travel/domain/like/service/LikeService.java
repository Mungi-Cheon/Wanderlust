package com.travel.domain.like.service;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.like.dto.request.LikeRequest;
import com.travel.domain.like.dto.response.LikeQueryResponse;
import com.travel.domain.like.dto.response.LikeResponse;
import com.travel.domain.like.entity.Like;
import com.travel.domain.like.repository.LikeRepository;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.type.ErrorType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final AccommodationRepository accommodationRepository;


    @Transactional
    @CacheEvict(value = "likedAccommodations", key = "#userId")
    public LikeResponse clickLike(Long userId, LikeRequest request) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new MemberException(ErrorType.INVALID_EMAIL_AND_PASSWORD));
        Accommodation accommodation = accommodationRepository.findById(request.getAccommodationId())
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));

        LikeQueryResponse likeQueryResponse = likeRepository
            .findLikeAndCountByMemberAndAccommodation(member, accommodation);
        boolean liked;

        if (likeQueryResponse.getLike() != null) {
            likeRepository.delete(likeQueryResponse.getLike());
            liked = false;
        } else {
            Like newLike = Like.builder()
                .member(member)
                .accommodation(accommodation)
                .build();
            likeRepository.save(newLike);
            liked = true;
        }

        int likeCount = likeQueryResponse.getTotalLikes();
        likeCount = liked ? likeCount + 1 : likeCount - 1;

        return new LikeResponse(liked, likeCount);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "likedAccommodations", key = "#userId")
    public List<AccommodationResponse> getLikedAccommodations(Long userId) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new MemberException(ErrorType.INVALID_EMAIL_AND_PASSWORD));
        List<Accommodation> likedAccommodations = likeRepository.findByMember(member).stream()
            .map(Like::getAccommodation)
            .toList();
        return likedAccommodations.stream()
            .map(AccommodationResponse::createAccommodationResponse)
            .collect(Collectors.toList());
    }
}
