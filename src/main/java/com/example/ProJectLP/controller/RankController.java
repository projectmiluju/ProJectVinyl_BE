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

    @RequestMapping(value = "/rank/vinyl", method = RequestMethod.GET)
    public ResponseDto<?> getRankVinyl() {

        return rankService.rankVinyl();
    }

}
