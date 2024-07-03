package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.domain.vinylLike.VinylLike;
import com.example.ProJectLP.domain.vinylLike.VinylLikeRepository;
import com.example.ProJectLP.dto.response.VinylRankResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Component
public class RankService {

    private final VinylRepository vinylRepository;
    private final VinylLikeRepository vinylLikeRepository;


    public ResponseEntity<?> rankVinylLike() {
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
                    .rank(i+1)
                    .title(dtoList.get(i).getTitle())
                    .artist(dtoList.get(i).getArtist())
                    .numLikes(dtoList.get(i).getNumLikes())
                    .numView(dtoList.get(i).getNumView())
                    .imageUrl(dtoList.get(i).getImageUrl())
                    .build();
            rankDtoList.add(rankDto);
            if (i == 9) break;
        }
        return ResponseEntity.ok(Map.of("msg", "바이닐 좋아요 랭킹 조회가 완료 됐습니다.", "data", rankDtoList));
    }

    public ResponseEntity<?> rankVinylLikeMonth() {
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
                    .rank(i+1)
                    .title(dtoList.get(i).getTitle())
                    .artist(dtoList.get(i).getArtist())
                    .numLikes(dtoList.get(i).getNumLikes())
                    .numView(dtoList.get(i).getNumView())
                    .imageUrl(dtoList.get(i).getImageUrl())
                    .build();
            rankDtoList.add(rankDto);
            if (i == 9) break;
        }

        return ResponseEntity.ok(Map.of("msg","최근 한달 기준 바이닐 좋아요 랭킹 조회가 완료 됐습니다.","data",rankDtoList));
    }

    public ResponseEntity<?> rankVinylView() {
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
                    .rank(i+1)
                    .title(dtoList.get(i).getTitle())
                    .artist(dtoList.get(i).getArtist())
                    .numLikes(dtoList.get(i).getNumLikes())
                    .numView(dtoList.get(i).getNumView())
                    .imageUrl(dtoList.get(i).getImageUrl())
                    .build();
            rankDtoList.add(rankDto);
            if (i == 9) break;
        }

        return ResponseEntity.ok(Map.of("msg","바이닐 조회수 랭킹 조회가 완료 됐습니다.","data",rankDtoList));
    }
}
