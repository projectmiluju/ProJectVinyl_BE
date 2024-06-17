package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.request.VinylRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class VinylService {

    private final VinylRepository vinylRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> uploadVinyl(VinylRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("400",
                    "Login is required.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("400",
                    "Login is required.");
        }

        Member member = validateMember(request);


        if (null == member) {
            return ResponseDto.fail("400", "INVALID_TOKEN");
        }

        if (member.isRole()){
            Vinyl vinyl = Vinyl.builder()
                    .title(requestDto.getTitle())
                    .description(requestDto.getDescription())
                    .artist(requestDto.getArtist())
                    .genre(requestDto.getGenre())
                    .build();

            vinylRepository.save(vinyl);

            return ResponseDto.success(
                    VinylResponseDto.builder()
                            .id(vinyl.getId())
                            .title(vinyl.getTitle())
                            .description(vinyl.getDescription())
                            .artist(vinyl.getArtist())
                            .genre(vinyl.getGenre())
                            .createdAt(vinyl.getCreatedAt())
                            .modifiedAt(vinyl.getModifiedAt())
                            .build()
            );
        }
        else return ResponseDto.fail("400", "관리자가 아닙니다.");

    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
