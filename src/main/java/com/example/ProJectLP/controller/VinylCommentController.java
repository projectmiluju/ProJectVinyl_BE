package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.VinylCommentRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.VinylCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class  VinylCommentController {

    private final VinylCommentService vinylCommentService;


    //vinyl 댓글 등록
    @RequestMapping(value = "/upload/vinyl/{vinylId}/comment", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVinylComment(@PathVariable Long vinylId,
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

    //vinyl 댓글 수정
    @RequestMapping(value = "/update/vinyl/{vinylId}/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updateVinylComment(@PathVariable Long vinylId,
                                             @PathVariable Long id,
                                             @RequestBody VinylCommentRequestDto requestDto,
                                             HttpServletRequest request){
        return vinylCommentService.updateVinylComment(vinylId, id, requestDto, request);
    }

    //vinyl 댓글 조회
    @RequestMapping(value = "/get/vinyl/{vinylId}/commentlist", method = RequestMethod.GET)
    public ResponseDto<?> getVinylCommentList(@PathVariable Long vinylId,
                                              @RequestParam("pageNum") int page,
                                              @RequestParam("pageLimit") int limit){

        page = page -1;

        return vinylCommentService.getVinylCommentList(vinylId, page, limit);
    }
}
