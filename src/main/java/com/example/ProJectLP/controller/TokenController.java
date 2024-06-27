package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.TokenReissueDto;

import com.example.ProJectLP.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @RequestMapping(value = "/api/token", method = RequestMethod.POST)
    public ResponseEntity<?> reissueToken(@RequestBody TokenReissueDto tokenReissueDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response){
        return tokenService.reissue(tokenReissueDto,request,response);
    }
}
