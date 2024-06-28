package com.travel.domain.like.repository;

import com.travel.domain.like.entity.Like;
import com.travel.domain.user.entity.User;
import com.travel.domain.accommodation.entity.Accommodation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUser(User user);
    Optional<Like> findByUserAndAccommodation(User user, Accommodation accommodation);
    int countByAccommodation(Accommodation accommodation);
}
