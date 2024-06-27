package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.member.MemberRepository;
import com.example.ProJectLP.dto.request.TokenDto;
import com.example.ProJectLP.dto.request.MemberRequestDto;
import com.example.ProJectLP.dto.request.SignInRequestDto;
import com.example.ProJectLP.dto.response.MemberResponseDto;
import com.example.ProJectLP.dto.response.ResponseDto;

import com.example.ProJectLP.exception.ErrorCode;
import com.example.ProJectLP.exception.PrivateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MailSendService mailSendService;


    //회원가입
    @Transactional
    public ResponseEntity<?> createMember(MemberRequestDto requestDto) {

        if (null != isPresentMemberByUsername(requestDto.getUsername())) {
            throw new PrivateException(ErrorCode.SIGNUP_ALREADY_USERNAME);
        }
        if (null != isPresentMemberByEmail(requestDto.getEmail())) {
            throw new PrivateException(ErrorCode.SIGNUP_ALREADY_EMAIL);
        }
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new PrivateException(ErrorCode.SIGNUP_PASSWORD_CHECK);
        }
        if (requestDto.getUsername().isBlank()) {
            throw new PrivateException(ErrorCode.SIGNUP_EMPTY_EMAIL);
        }
        if (requestDto.getEmail().isBlank()) {
            throw new PrivateException(ErrorCode.SIGNUP_EMPTY_EMAIL);
        }
        if (requestDto.getPassword().isBlank()) {
            throw new PrivateException(ErrorCode.SIGNUP_EMPTY_PASSWORD);
        }

        boolean emailCheck = mailSendService.emailCheck(requestDto.getEmail(), requestDto.getAuthNum());
        if (!emailCheck){
            throw new PrivateException(ErrorCode.SIGNUP_EMAIL_CHECK);
        }

        Member member = Member.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .emailCheck(emailCheck)
                .build();
        memberRepository.save(member);

        return ResponseEntity.ok(Map.of("msg", member.getUsername()+"님 회원가입이 완료 됐습니다."));
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


//        if (refreshTokenRepository.findByMemberId(member.getId()).isEmpty()) {
            TokenDto tokenDto = tokenProvider.generateTokenDto(member);
            tokenToHeaders(tokenDto, response);
//        }else {
//            deleteRefreshToken(member);
//            if (refreshTokenRepository.findByMemberId(member.getId()).isEmpty()) {
//                TokenDto tokenDto = tokenProvider.generateTokenDto(member);
//                tokenToHeaders(tokenDto, response);
//            }
//        }

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

        return tokenProvider.deleteRefreshToken(request,member);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    // 아이디 중복검사
    @Transactional(readOnly = true)
    public Member isPresentMemberByUsername(String username) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        return optionalMember.orElse(null);
    }
    @Transactional
    public ResponseEntity<?> checkUsername(String username){
        Member member = isPresentMemberByUsername(username);
        if(member == null) return ResponseEntity.ok(Map.of("msg", "가입 가능한 아이디입니다."));
        else throw new PrivateException(ErrorCode.SIGNUP_ALREADY_USERNAME);
    }

    // 메일 중복검사
    @Transactional(readOnly = true)
    public Member isPresentMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }
    @Transactional
    public ResponseEntity<?> checkEmail(String email){
        Member member = isPresentMemberByEmail(email);
        if(member == null) return ResponseEntity.ok(Map.of("msg", "가입 가능한 이메일입니다."));
        else throw new PrivateException(ErrorCode.SIGNUP_ALREADY_EMAIL);
    }

}