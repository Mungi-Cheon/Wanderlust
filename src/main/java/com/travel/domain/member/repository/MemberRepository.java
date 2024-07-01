package com.travel.domain.member.repository;

import com.travel.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailOrderByIdDesc(String email);
}
