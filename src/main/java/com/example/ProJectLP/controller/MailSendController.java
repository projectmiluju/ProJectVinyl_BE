package com.example.ProJectLP.controller;

import com.example.ProJectLP.dto.request.EmailRequestDto;
import com.example.ProJectLP.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MailSendController {

    private final MailSendService mailService;

    @RequestMapping(value = "/api/email/send", method = RequestMethod.POST)
    public ResponseEntity<?> mailSend(@RequestBody @Valid EmailRequestDto emailDto){
        return mailService.joinEmail(emailDto.getEmail());
    }

}
