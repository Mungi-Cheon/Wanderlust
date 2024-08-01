package com.travel.domain.member.service;

import static com.travel.global.exception.type.ErrorType.DUPLICATED_MEMBER;
import static com.travel.global.exception.type.ErrorType.INVALID_EMAIL_AND_PASSWORD;
import static com.travel.global.security.type.TokenType.ACCESS;
import static com.travel.global.security.type.TokenType.REFRESH;

import com.travel.domain.member.dto.request.LoginRequest;
import com.travel.domain.member.dto.request.SignupRequest;
import com.travel.domain.member.dto.response.LoginResponse;
import com.travel.domain.member.dto.response.MemberResponse;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.global.exception.MemberException;
import com.travel.global.security.token.service.TokenService;
import com.travel.global.security.type.TokenType;
import com.travel.global.util.CookieUtil;
import com.travel.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    private final TokenService tokenService;

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
        Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new MemberException(INVALID_EMAIL_AND_PASSWORD));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(INVALID_EMAIL_AND_PASSWORD);
        }

        String refreshToken = tokenService.getRefreshToken(member.getEmail());
        String accessToken = null;
        if (refreshToken == null) {
            refreshToken = jwtUtil.generateRefreshToken(member.getId());
        }
        accessToken = jwtUtil.generateAccessToken(member.getId());

        generateToken(response, ACCESS, accessToken);
        generateToken(response, REFRESH, refreshToken);

        tokenService.saveRefreshToken(member.getEmail(), refreshToken);

        return new LoginResponse(accessToken);
    }

    @Transactional
    public void deleteMember(HttpServletRequest request, HttpServletResponse response,
        Long tokenMemberId) {
        Member member = memberRepository.findById(tokenMemberId)
            .orElseThrow(() -> new MemberException(INVALID_EMAIL_AND_PASSWORD));
        String refreshToken = cookieUtil.getTokenFromCookies(request.getCookies(),
            REFRESH.getName());

        tokenService.removeToken(member.getEmail());
        deleteCookies(response);

        memberRepository.delete(member);
    }

    private void deleteCookies(HttpServletResponse response) {
        cookieUtil.removeCookie(response, REFRESH.getName());
        cookieUtil.removeCookie(response, ACCESS.getName());
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

        if (tokenType.getName().equals(ACCESS.getName())) {
            cookieUtil.createAccessTokenCookie(response, tokenType.getName(), token);
        } else {
            cookieUtil.createRefreshTokenCookie(response, tokenType.getName(), token);
        }
    }
}

