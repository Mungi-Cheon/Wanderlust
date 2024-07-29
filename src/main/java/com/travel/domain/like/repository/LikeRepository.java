package com.travel.domain.like.repository;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.like.dto.response.LikeQueryResponse;
import com.travel.domain.like.entity.Like;
import com.travel.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByMember(Member member);
    Optional<Like> findByMemberAndAccommodation(Member member, Accommodation accommodation);
    int countByAccommodation(Accommodation accommodation);

    @Query(value = "SELECT new com.travel.domain.like.dto.response.LikeQueryResponse(l, " +
        "(SELECT COUNT(l2) FROM Like l2 WHERE l2.accommodation = :accommodation)) " +
        "FROM Like l " +
        "WHERE l.member = :member AND l.accommodation = :accommodation", nativeQuery = true)
    LikeQueryResponse findLikeAndCountByMemberAndAccommodation(@Param("member") Member member, @Param("accommodation") Accommodation accommodation);

}
