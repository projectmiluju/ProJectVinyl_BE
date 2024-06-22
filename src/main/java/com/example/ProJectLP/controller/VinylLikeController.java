package com.example.ProJectLP.controller;

import com.example.ProJectLP.domain.jwt.TokenProvider;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.VinylLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VinylLikeController {

    private final VinylLikeService vinylLikeService;
    private final TokenProvider tokenProvider;

    //vinyl 좋아요 등록
    @RequestMapping(value = "/upload/vinyl/{vinylId}/like", method = RequestMethod.POST)
    public ResponseDto<?> likeVinyl(@PathVariable Long vinylId, HttpServletRequest request) {
        return vinylLikeService.likeVinyl(vinylId, request);
    }


    //vinyl 좋아요 삭제
    @RequestMapping(value = "/delete/vinyl/{vinylId}/like", method = RequestMethod.DELETE)
    public ResponseDto<?> unlikeVinyl(@PathVariable Long vinylId, HttpServletRequest request) {
        return vinylLikeService.unlikeVinyl(vinylId, request);
    }
}
