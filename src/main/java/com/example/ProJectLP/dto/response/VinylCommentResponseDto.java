package com.example.ProJectLP.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VinylCommentResponseDto {

    private Long id;
    private String content;
    private String username;

    private boolean IsMine;
}
