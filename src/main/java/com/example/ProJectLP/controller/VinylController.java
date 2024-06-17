package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.VinylRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.VinylService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class VinylController {

    private final VinylService vinylService;

    @RequestMapping(value = "/upload/vinyl", method = RequestMethod.POST)
    public ResponseDto<?> uploadVinyl(@RequestBody VinylRequestDto requestDto, HttpServletRequest request){
        return vinylService.uploadVinyl(requestDto, request);
    }
}
