package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.member.Member;
import com.example.ProJectLP.domain.member.MemberRepository;
import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.request.VinylRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VinylService {

    private final VinylRepository vinylRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> uploadVinyl(VinylRequestDto requestDto, UserDetails user) {
        Optional<Member> member = memberRepository.findByUsername(user.getUsername());

        if (member.get().isRole()){
            Vinyl vinyl = Vinyl.builder()
                    .title(requestDto.getTitle())
                    .description(requestDto.getDescription())
                    .artist(requestDto.getArtist())
                    .build();

            vinylRepository.save(vinyl);

            return ResponseDto.success(
                    VinylResponseDto.builder()
                            .id(vinyl.getId())
                            .title(vinyl.getTitle())
                            .description(vinyl.getDescription())
                            .artist(vinyl.getArtist())
                            .build()
            );
        }
        else return ResponseDto.fail("400", "관리자가 아닙니다.");

    }
}
