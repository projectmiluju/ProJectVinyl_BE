package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.member.MemberRepository;
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

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail("400",
                    "Password and Confirm Password do not match");
        }

        Member member = Member.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .role(member.isRole())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

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


        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder().
                        id(member.getId())
                        .username(member.getUsername())
                        .role(member.isRole())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );

    }

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

}