package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.MemberRequestDto;
import com.example.ProJectLP.dto.request.SignInRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value = "/api/signup", method = RequestMethod.POST)
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    @RequestMapping(value = "/api/signin", method = RequestMethod.POST)
    public ResponseDto<?> signin(@RequestBody @Valid SignInRequestDto requestDto, HttpServletRequest request) {
        return memberService.loginMember(requestDto, request);
    }

}