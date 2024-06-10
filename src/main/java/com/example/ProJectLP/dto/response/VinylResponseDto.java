package com.example.ProJectLP.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VinylResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String artist;
}
