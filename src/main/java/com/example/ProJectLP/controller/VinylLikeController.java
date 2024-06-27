package com.example.ProJectLP.controller;

import com.example.ProJectLP.service.VinylLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VinylLikeController {

    private final VinylLikeService vinylLikeService;
    //vinyl 좋아요 등록
    @RequestMapping(value = "/upload/vinyl/{vinylId}/like", method = RequestMethod.POST)
    public ResponseEntity<?> likeVinyl(@PathVariable Long vinylId, HttpServletRequest request) {
        return vinylLikeService.likeVinyl(vinylId, request);
    }


    //vinyl 좋아요 삭제
    @RequestMapping(value = "/delete/vinyl/{vinylId}/like", method = RequestMethod.DELETE)
    public ResponseEntity<?> unlikeVinyl(@PathVariable Long vinylId, HttpServletRequest request) {
        return vinylLikeService.unlikeVinyl(vinylId, request);
    }
}
