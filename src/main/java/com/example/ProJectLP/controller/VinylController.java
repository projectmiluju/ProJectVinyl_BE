package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.VinylRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.VinylService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
public class VinylController {

    private final VinylService vinylService;

    @RequestMapping(value = "/upload/vinyl", method = RequestMethod.POST)
    public ResponseDto<?> uploadVinyl(@RequestPart VinylRequestDto requestDto, @RequestPart MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        return vinylService.uploadVinyl(requestDto, multipartFile, request);
    }
}
