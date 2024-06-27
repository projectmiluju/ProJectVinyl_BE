package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.member.MemberRepository;
import com.example.ProJectLP.dto.request.TokenDto;
import com.example.ProJectLP.dto.request.TokenReissueDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.exception.ErrorCode;
import com.example.ProJectLP.exception.PrivateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;


    public ResponseEntity<?> reissue(TokenReissueDto tokenReissueDto,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {

        Member member = memberRepository.findByUsername(tokenReissueDto.getUsername()).get();

        if (refreshTokenService.getData(request.getHeader("RefreshToken")).equals(member.getId().toString())) {
            if (tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
                TokenDto tokenDto = tokenProvider.generateTokenDto(member);
                response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
                response.addHeader("RefreshToken", tokenDto.getRefreshToken());
                response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
                refreshTokenService.deleteData(request.getHeader("RefreshToken"));
                return ResponseEntity.ok(Map.of("msg","토큰 재발급이 완료 됐습니다."));
            }
        }
        throw new PrivateException(ErrorCode.TOKEN_REISSUE);
    }
}
