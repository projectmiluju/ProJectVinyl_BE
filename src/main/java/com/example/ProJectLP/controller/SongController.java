package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.SongRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.SongService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class SongController {

    private final SongService songService;


    //트랙리스트 업데이트
    @RequestMapping(value = "/update/song/{vinylid}", method = RequestMethod.PUT)
    public ResponseDto<?> updateSong(@PathVariable Long vinylid, @RequestBody SongRequestDto songs, HttpServletRequest request) {
        return songService.updateSong(vinylid, songs, request);
    }

}
