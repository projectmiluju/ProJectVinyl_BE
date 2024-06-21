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
    @RequestMapping(value = "/upload/vinyl/{vinylId}/comment", method = RequestMethod.POST)
    public ResponseDto<?> uploadVinylComment(@PathVariable Long vinylId,
                                             @RequestBody VinylCommentRequestDto requestDto,
                                             HttpServletRequest request){
        return vinylCommentService.uploadVinylComment(vinylId,requestDto,request);
    }

    //vinyl 댓글 삭제
    @RequestMapping(value = "/delete/vinyl/{vinylId}/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteVinylComment(@PathVariable Long vinylId,
                                             @PathVariable Long id,
                                             HttpServletRequest request){
        return vinylCommentService.deleteVinylComment(vinylId, id, request);
    }
}
