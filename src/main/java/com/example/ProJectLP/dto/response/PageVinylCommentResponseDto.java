package com.example.ProJectLP.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.stream.events.Comment;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageVinylCommentResponseDto {

    private int currPage;
    private int totalPage;
    private int currContent;
    private List<VinylCommentResponseDto> vinylCommentList;
}
