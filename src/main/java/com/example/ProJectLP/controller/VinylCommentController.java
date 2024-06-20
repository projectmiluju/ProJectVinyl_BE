package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.VinylCommentRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.VinylCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class VinylCommentController {

    private final VinylCommentService vinylCommentService;


    //vinyl 댓글 등록
    @RequestMapping(value = "/upload/vinyl/{vinylid}/comment", method = RequestMethod.POST)
    public ResponseDto<?> uploadVinylComment(@PathVariable Long vinylid,
                                             @RequestBody VinylCommentRequestDto requestDto,
                                             HttpServletRequest request){
        return vinylCommentService.uploadVinylComment(vinylid,requestDto,request);
    }
}
