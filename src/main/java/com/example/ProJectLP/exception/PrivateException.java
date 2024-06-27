package com.example.ProJectLP.exception;

import lombok.Getter;

@Getter
public class PrivateException extends RuntimeException {

    private final ErrorCode errorCode;

    public PrivateException(ErrorCode errorCode) {
        super(errorCode.name()+" : "+errorCode.getErrorMsg());
        this.errorCode = errorCode;
    }
}
