package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylRankResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankService {

    private final VinylRepository vinylRepository;

    @Transactional
    public ResponseDto<?> rankVinyl() {
        List<Vinyl> vinylList = vinylRepository.findAll();
        List<VinylRankResponseDto> dtoList = new ArrayList<>();

        for (Vinyl vinyl : vinylList) {
            VinylRankResponseDto dto = VinylRankResponseDto.builder()
                    .id(vinyl.getId())
                    .title(vinyl.getTitle())
                    .artist(vinyl.getArtist())
                    .imageUrl(vinyl.getImageUrl())
                    .numLikes(vinyl.getVinylLikes().size())
                    .build();
            dtoList.add(dto);
        }

        dtoList = dtoList.stream().sorted(Comparator.comparing(VinylRankResponseDto::getNumLikes).reversed()).collect(Collectors.toList());

        List<VinylRankResponseDto> rankDtoList = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            VinylRankResponseDto rankDto = VinylRankResponseDto.builder()
                    .id(dtoList.get(i).getId())
                    .title(dtoList.get(i).getTitle())
                    .artist(dtoList.get(i).getArtist())
                    .numLikes(dtoList.get(i).getNumLikes())
                    .imageUrl(dtoList.get(i).getImageUrl())
                    .build();
            rankDtoList.add(rankDto);
            if (i == 9) break;
        }

        return ResponseDto.success(rankDtoList);
    }
}
