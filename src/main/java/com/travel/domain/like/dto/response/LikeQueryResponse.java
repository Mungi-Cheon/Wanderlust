package com.travel.domain.like.dto.response;

import com.travel.domain.like.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeQueryResponse {
    private Like like;
    private int totalLikes;
}
