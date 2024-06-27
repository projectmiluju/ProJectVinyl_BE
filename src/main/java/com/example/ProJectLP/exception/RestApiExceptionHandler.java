package com.example.ProJectLP.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { PrivateException.class })
    public ResponseEntity<?> handleApiRequestException(PrivateException ex) {
        String errCode = ex.getErrorCode().getErrorCode();
        String errMsg = ex.getErrorCode().getErrorMsg();
        PrivateResponseDto privateResponseDto = PrivateResponseDto.builder().errorCode(errCode).errorMsg(errMsg).build();
//        Sentry.captureException(ex); 추후 sentry 적용
        return new ResponseEntity<>(
                privateResponseDto,
                ex.getErrorCode().getHttpStatus()
        );
    }
}
