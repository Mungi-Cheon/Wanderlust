package com.travel.domain.member.service;

import static com.travel.global.exception.type.ErrorType.DUPLICATED_MEMBER;
import static com.travel.global.exception.type.ErrorType.NONEXISTENT_MEMBER;
import static com.travel.global.exception.type.ErrorType.NOT_CORRECT_PASSWORD;

import com.travel.domain.member.dto.request.LoginRequest;
import com.travel.domain.member.dto.request.SignupRequest;
import com.travel.domain.member.dto.response.LoginDto;
import com.travel.domain.member.dto.response.MemberResponse;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.global.annotation.TokenMemberId;
import com.travel.global.exception.MemberException;
import com.travel.global.jwt.JwtProvider;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public MemberResponse signup(SignupRequest request) {
        String email = request.getEmail();

        boolean isExist = memberRepository.existsByEmail(email);

        if (isExist) {
            throw new MemberException(DUPLICATED_MEMBER);
        }
        String password = passwordEncoder.encode(request.getPassword());
        Member entity = Member.from(request, password);

        Member savedMember = memberRepository.save(entity);
        return MemberResponse.from(savedMember);
    }

    public LoginDto login(LoginRequest request) {
        List<Member> members = memberRepository.findByEmailOrderByIdDesc(request.getEmail());
        if (members.isEmpty()) {
            throw new MemberException(NONEXISTENT_MEMBER);
        }

        Member member = members.get(0);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(NOT_CORRECT_PASSWORD);
        }

        String accessToken = jwtProvider.generateAccessToken(member.getId());
        return new LoginDto(accessToken);
    }

    @Transactional
    public void deleteMember(@TokenMemberId Long tokenMemberId) {
        Member member = memberRepository.findById(tokenMemberId)
            .orElseThrow(() -> new MemberException(NONEXISTENT_MEMBER));

        memberRepository.delete(member);
        }

    @Transactional
    public void permanentlyDeleteOldMembers() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Member> membersDelete = memberRepository.findByDeletedAtIsNotNullAndDeletedAtBefore(oneMonthAgo);

        for (Member member : membersDelete) {
            memberRepository.delete(member);
        }
    }
}

