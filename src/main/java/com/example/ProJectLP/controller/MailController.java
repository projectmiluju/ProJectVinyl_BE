package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.EmailRequestDto;
import com.example.ProJectLP.dto.response.ResponseDto;
import com.example.ProJectLP.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailSendService mailService;

    @RequestMapping(value = "/api/email", method = RequestMethod.POST)
    public ResponseDto<?> mailSend(@RequestBody @Valid EmailRequestDto emailDto){
        return mailService.joinEmail(emailDto.getEmail());
    }

}
