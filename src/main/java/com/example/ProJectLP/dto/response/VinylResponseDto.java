package com.example.ProJectLP.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VinylResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String artist;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
