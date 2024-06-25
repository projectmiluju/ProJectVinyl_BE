package com.example.ProJectLP.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VinylRankResponseDto {
    private Long id;
    private String title;
    private String artist;
    private String imageUrl;
    private Integer numLikes;
    private Integer numView;
}
