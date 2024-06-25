package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.domain.vinylLike.VinylLike;
import com.example.ProJectLP.domain.vinylLike.VinylLikeRepository;
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
    private final VinylLikeRepository vinylLikeRepository;

    @Transactional
    public ResponseDto<?> rankVinylLike() {
        List<Vinyl> vinylList = vinylRepository.findAll();
        List<VinylRankResponseDto> dtoList = new ArrayList<>();

        for (Vinyl vinyl : vinylList) {
            if (!vinyl.getVinylLikes().isEmpty()){
                VinylRankResponseDto dto = VinylRankResponseDto.builder()
                        .id(vinyl.getId())
                        .title(vinyl.getTitle())
                        .artist(vinyl.getArtist())
                        .imageUrl(vinyl.getImageUrl())
                        .numLikes(vinyl.getVinylLikes().size())
                        .numView(vinyl.getView())
                        .build();
                dtoList.add(dto);
            }
        }

        dtoList = dtoList.stream().sorted(Comparator.comparing(VinylRankResponseDto::getNumLikes).reversed()).collect(Collectors.toList());

        List<VinylRankResponseDto> rankDtoList = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            VinylRankResponseDto rankDto = VinylRankResponseDto.builder()
                    .id(dtoList.get(i).getId())
                    .title(dtoList.get(i).getTitle())
                    .artist(dtoList.get(i).getArtist())
                    .numLikes(dtoList.get(i).getNumLikes())
                    .numView(dtoList.get(i).getNumView())
                    .imageUrl(dtoList.get(i).getImageUrl())
                    .build();
            rankDtoList.add(rankDto);
            if (i == 9) break;
        }

        return ResponseDto.success(rankDtoList);
    }

    @Transactional
    public ResponseDto<?> rankVinylLikeMonth() {
        List<Vinyl> vinylList = vinylRepository.findAll();
        List<VinylRankResponseDto> dtoList = new ArrayList<>();

        for (Vinyl vinyl : vinylList) {
            List<VinylLike> vinylLikes = vinylLikeRepository.findByVinylId(vinyl.getId());
            if (!vinylLikes.isEmpty()) {
                VinylRankResponseDto dto = VinylRankResponseDto.builder()
                        .id(vinyl.getId())
                        .title(vinyl.getTitle())
                        .artist(vinyl.getArtist())
                        .imageUrl(vinyl.getImageUrl())
                        .numLikes(vinylLikes.size())
                        .numView(vinyl.getView())
                        .build();
                dtoList.add(dto);
            }
        }

        dtoList = dtoList.stream().sorted(Comparator.comparing(VinylRankResponseDto::getNumLikes).reversed()).collect(Collectors.toList());

        List<VinylRankResponseDto> rankDtoList = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            VinylRankResponseDto rankDto = VinylRankResponseDto.builder()
                    .id(dtoList.get(i).getId())
                    .title(dtoList.get(i).getTitle())
                    .artist(dtoList.get(i).getArtist())
                    .numLikes(dtoList.get(i).getNumLikes())
                    .numView(dtoList.get(i).getNumView())
                    .imageUrl(dtoList.get(i).getImageUrl())
                    .build();
            rankDtoList.add(rankDto);
            if (i == 9) break;
        }

        return ResponseDto.success(rankDtoList);
    }

    @Transactional
    public ResponseDto<?> rankVinylView() {
        List<Vinyl> vinylList = vinylRepository.findAll();
        List<VinylRankResponseDto> dtoList = new ArrayList<>();

        for (Vinyl vinyl : vinylList) {
            if (vinyl.getView() != 0){
                VinylRankResponseDto dto = VinylRankResponseDto.builder()
                        .id(vinyl.getId())
                        .title(vinyl.getTitle())
                        .artist(vinyl.getArtist())
                        .imageUrl(vinyl.getImageUrl())
                        .numLikes(vinyl.getVinylLikes().size())
                        .numView(vinyl.getView())
                        .build();
                dtoList.add(dto);
            }
        }

        dtoList = dtoList.stream().sorted(Comparator.comparing(VinylRankResponseDto::getNumView).reversed()).collect(Collectors.toList());

        List<VinylRankResponseDto> rankDtoList = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            VinylRankResponseDto rankDto = VinylRankResponseDto.builder()
                    .id(dtoList.get(i).getId())
                    .title(dtoList.get(i).getTitle())
                    .artist(dtoList.get(i).getArtist())
                    .numLikes(dtoList.get(i).getNumLikes())
                    .numView(dtoList.get(i).getNumView())
                    .imageUrl(dtoList.get(i).getImageUrl())
                    .build();
            rankDtoList.add(rankDto);
            if (i == 9) break;
        }

        return ResponseDto.success(rankDtoList);
    }
}
