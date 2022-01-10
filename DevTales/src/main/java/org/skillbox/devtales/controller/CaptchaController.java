package org.skillbox.devtales.controller;

import lombok.RequiredArgsConstructor;
import org.skillbox.devtales.api.response.AuthCaptchaResponse;
import org.skillbox.devtales.service.CaptchaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/captcha")
    private ResponseEntity<AuthCaptchaResponse> getCaptcha() throws IOException {
        return new ResponseEntity<>(captchaService.getCaptcha(), HttpStatus.OK);
    }
}
