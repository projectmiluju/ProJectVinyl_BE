package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.member.MemberRepository;
import com.example.ProJectLP.dto.request.TokenDto;
import com.example.ProJectLP.dto.request.TokenReissueDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;


    public ResponseDto<?> reissue(TokenReissueDto tokenReissueDto,
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
                return ResponseDto.success("Successfully token reissued");
            }
        }
        return ResponseDto.fail("400", "unSuccessfully token reissued");
    }
}
