package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.AuthCaptchaResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface CaptchaService {
    AuthCaptchaResponse getCaptcha() throws IOException;
}
