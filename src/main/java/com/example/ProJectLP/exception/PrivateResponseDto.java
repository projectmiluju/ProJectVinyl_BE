package com.example.ProJectLP.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PrivateResponseDto {
    private String errorCode;
    private String errorMsg;
}
