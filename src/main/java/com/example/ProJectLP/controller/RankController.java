package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    // 전체 기간 좋아요
    @RequestMapping(value = "/rank/vinyl/like", method = RequestMethod.GET)
    public ResponseDto<?> getRankVinylLike() {

        return rankService.rankVinylLike();
    }

    // 특정 기간 좋아요
    @RequestMapping(value = "/rank/vinyl/like/month", method = RequestMethod.GET)
    public ResponseDto<?> getRankVinylLikeMonth() {

        return rankService.rankVinylLikeMonth();
    }

    // 전체 기간 조회수
    @RequestMapping(value = "/rank/vinyl/view", method = RequestMethod.GET)
    public ResponseDto<?> getRankVinylView() {

        return rankService.rankVinylView();
    }


}
