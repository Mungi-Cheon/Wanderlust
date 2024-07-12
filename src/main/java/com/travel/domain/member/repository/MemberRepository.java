package com.travel.domain.member.repository;

import com.travel.domain.member.entity.Member;
import com.travel.global.annotation.TokenMemberId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    List<Member> findByEmailOrderByIdDesc(String email);

    List<Member> findByDeletedIsTrueAndDeletedAtBefore(LocalDateTime dateTime);

    @Transactional
    void delete(@TokenMemberId Long tokenMemberId);
}
