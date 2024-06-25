package com.example.ProJectLP.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto {

    private int currPage;
    private int totalPage;
    private int currContent;
    private List<VinylListResponseDto> vinylList;

}
