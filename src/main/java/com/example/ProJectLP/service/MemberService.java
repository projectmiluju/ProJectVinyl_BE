package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.member.MemberRepository;
import com.example.ProJectLP.domain.refreshToken.RefreshToken;
import com.example.ProJectLP.domain.refreshToken.RefreshTokenRepository;
import com.example.ProJectLP.dto.TokenDto;
import com.example.ProJectLP.dto.request.MemberRequestDto;
import com.example.ProJectLP.dto.request.SignInRequestDto;
import com.example.ProJectLP.dto.response.MemberResponseDto;
import com.example.ProJectLP.dto.response.ResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailSendService mailSendService;


    //회원가입
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail("400",
                    "Password and Confirm Password do not match");
        }

        boolean emailCheck = mailSendService.emailCheck(requestDto.getEmail(), requestDto.getAuthNum());
        if (!emailCheck){
            return ResponseDto.fail("400", "Check your email and auth num");
        }

        Member member = Member.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .emailCheck(emailCheck)
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .role(member.isRole())
                        .emailCheck(member.isEmailCheck())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    //로그인
    @Transactional
    public ResponseDto<?> loginMember(SignInRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMemberByUsername(requestDto.getUsername());

        if (null == member) {
            return ResponseDto.fail("400",
                    "User not found");
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("400", "Password error");
        }

        if (!member.isEmailCheck()){
            return ResponseDto.fail("400", "Check your email and auth num");
        }

        if (refreshTokenRepository.findByMemberId(member.getId()).isEmpty()) {
            TokenDto tokenDto = tokenProvider.generateTokenDto(member);
            tokenToHeaders(tokenDto, response);
        }else {
            deleteRefreshToken(member);
            if (refreshTokenRepository.findByMemberId(member.getId()).isEmpty()) {
                TokenDto tokenDto = tokenProvider.generateTokenDto(member);
                tokenToHeaders(tokenDto, response);
            }
        }

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .role(member.isRole())
                        .emailCheck(member.isEmailCheck())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );

    }

    //로그아웃
    @Transactional
    public ResponseDto<?> logoutMember(HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return ResponseDto.fail("403", "Token is not valid");
        }

        Member member = tokenProvider.getMemberFromAuthentication();

        if (null == member) {
            return ResponseDto.fail("400",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }

    //
    @Transactional(readOnly = true)
    public Member isPresentMemberByUsername(String username) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    // 아이디 중복검사
    @Transactional
    public ResponseDto<?> checkUsername(String username){
        Member member = isPresentMemberByUsername(username);
        if(member == null) return ResponseDto.success(true);
        else return ResponseDto.fail("400","ID already exists");
    }

    @Transactional(readOnly = true)
    public RefreshToken isPresentRefreshToken(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMemberId(member.getId());
        return optionalRefreshToken.orElse(null);
    }

    @Transactional
    public void deleteRefreshToken(Member member) {
        RefreshToken refreshToken = isPresentRefreshToken(member);
        refreshTokenRepository.delete(refreshToken);
    }

}