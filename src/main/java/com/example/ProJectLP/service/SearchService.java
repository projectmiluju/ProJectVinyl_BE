package com.example.ProJectLP.service;

import com.example.ProJectLP.domain.vinyl.Vinyl;
import com.example.ProJectLP.domain.vinyl.VinylRepository;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.dto.response.VinylListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final VinylRepository vinylRepository;

    public ResponseDto<?> searchVinyl(String keyword) {

        List<Vinyl> vinylList = vinylRepository.findByTitle(keyword);
        List<VinylListResponseDto> vinylListResponseDtoList = new ArrayList<>();
        for (Vinyl vinyl : vinylList) {
            vinylListResponseDtoList.add(new VinylListResponseDto(vinyl));
        }
        return ResponseDto.success(vinylListResponseDtoList);
    }
}
