package com.example.ProJectLP.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    private String username;
    private String password;
    private String passwordConfirm;
    @Email
    private String email;
    private String authNum;
}
