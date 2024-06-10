package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.request.VinylRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VinylService {

    private final VinylRepository vinylRepository;

    @Transactional
    public ResponseDto<?> uploadVinyl(VinylRequestDto requestDto) {

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
}
