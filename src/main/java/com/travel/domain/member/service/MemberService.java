package com.travel.domain.member.service;

import static com.travel.global.exception.type.ErrorType.DUPLICATED_MEMBER;
import static com.travel.global.exception.type.ErrorType.NONEXISTENT_MEMBER;
import static com.travel.global.exception.type.ErrorType.NOT_CORRECT_PASSWORD;
import static com.travel.global.security.type.TokenType.ACCESS;
import static com.travel.global.security.type.TokenType.REFRESH;

import com.travel.domain.member.dto.request.LoginRequest;
import com.travel.domain.member.dto.request.SignupRequest;
import com.travel.domain.member.dto.response.LoginResponse;
import com.travel.domain.member.dto.response.MemberResponse;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.global.annotation.TokenMemberId;
import com.travel.global.exception.MemberException;
import com.travel.global.security.jwt.JwtUtil;
import com.travel.global.security.type.TokenType;
import com.travel.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    public MemberResponse signup(SignupRequest request) {
        String email = request.getEmail();

        if (memberRepository.existsByEmail(email)) {
            throw new MemberException(DUPLICATED_MEMBER);
        }

        String password = passwordEncoder.encode(request.getPassword());
        Member newMember = Member.from(request, password);

        Member savedMember = memberRepository.save(newMember);
        return MemberResponse.from(savedMember);
    }

    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        List<Member> members = memberRepository.findByEmailOrderByIdDesc(request.getEmail());
        if (members.isEmpty()) {
            throw new MemberException(NONEXISTENT_MEMBER);
        }

        Member member = members.get(0);
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(NOT_CORRECT_PASSWORD);
        }

        String accessToken = jwtUtil.generateAccessToken(member.getId());
        String refreshToken = jwtUtil.generateRefreshToken(member.getId());
        generateToken(response, ACCESS, accessToken);
        generateToken(response, REFRESH, refreshToken);

        return new LoginResponse(accessToken);
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
        List<Member> membersDelete = memberRepository.findByDeletedAtIsNotNullAndDeletedAtBefore(
            oneMonthAgo);

        for (Member member : membersDelete) {
            memberRepository.deleteByEmail(member.getEmail());
        }
    }

    private void generateToken(HttpServletResponse response, TokenType tokenType, String token) {
        Cookie cookie = null;

        if (tokenType.getName().equals(ACCESS.getName())) {
            cookie = cookieUtil.createAccessTokenCookie(tokenType.getName(),
                token);
        } else {
            cookie = cookieUtil.createRefreshTokenCookie(tokenType.getName(),
                token);
        }
        response.addCookie(cookie);
    }
}

