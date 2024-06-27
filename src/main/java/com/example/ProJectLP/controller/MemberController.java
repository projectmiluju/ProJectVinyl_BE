package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.MemberRequestDto;
import com.example.ProJectLP.dto.request.SignInRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @RequestMapping(value = "/api/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    //로그인
    @RequestMapping(value = "/api/signin", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequestDto requestDto, HttpServletResponse response) {
        return memberService.loginMember(requestDto, response);
    }

    //로그아웃
    @RequestMapping(value = "/api/signout", method = RequestMethod.GET)
    public ResponseDto<?> signOut(HttpServletRequest request) {
        return memberService.logoutMember(request);
    }

    //아이디 중복검사
    @RequestMapping(value = "/api/username", method = RequestMethod.POST)
    public ResponseEntity<?> usernameCheck(@RequestBody Map<String, String> body) {
        return memberService.checkUsername(body.get("username"));
    }

    //메일 중복검사
    @RequestMapping(value = "/api/email", method = RequestMethod.POST)
    public ResponseEntity<?> emailCheck(@RequestBody Map<String, String> body) {
        return memberService.checkEmail(body.get("email"));
    }



}
