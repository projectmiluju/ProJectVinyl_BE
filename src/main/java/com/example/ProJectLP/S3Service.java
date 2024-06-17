package com.example.ProJectLP;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String BUCKET;
    private final S3Operations s3Operations;

    public S3Service(S3Operations s3Operations) {
        this.s3Operations = s3Operations;
    }

    @Transactional
    public ResponseEntity<?> upload(MultipartFile multipartFile, String key) throws IOException {
        if (!MediaType.IMAGE_JPEG.toString().equals(multipartFile.getContentType())) {
            return ResponseEntity.badRequest().body("사진 파일만 업로드 가능합니다");
        }

        try (InputStream is = multipartFile.getInputStream()) {
            s3Operations.upload(BUCKET, key, is,
                    ObjectMetadata.builder().contentType(multipartFile.getContentType()).build());
        }

        return ResponseEntity.accepted().build();
    }
}
