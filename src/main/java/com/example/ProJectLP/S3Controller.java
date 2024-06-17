package com.example.ProJectLP;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @RequestMapping(value = "/test/upload", method = RequestMethod.POST)
    public ResponseEntity<?> upload(MultipartFile multipartFile, String key) throws IOException {
        return s3Service.upload(multipartFile,key);
    }
}
