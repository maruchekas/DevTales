package org.skillbox.devtales.service.impl;


import cn.apiclub.captcha.Captcha;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.api.response.AuthCaptchaResponse;
import org.skillbox.devtales.model.CaptchaCode;
import org.skillbox.devtales.repository.CaptchaRepository;
import org.skillbox.devtales.service.CaptchaService;
import org.skillbox.devtales.util.CaptchaUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Data
@Setter
@Getter
public class CaptchaServiceImpl implements CaptchaService {
    @Value("${captcha.image}")
    private String imageEncodeSuffix;
    @Value("${captcha.width-origin}")
    private int captchaOriginWidth;
    @Value("${captcha.height-origin}")
    private int captchaOriginHeight;
    @Value("${captcha.width-new}")
    private int captchaNewWidth;
    @Value("${captcha.height-new}")
    private int captchaNewHeight;
    private static LocalDateTime timeToDeleteOldCaptcha = LocalDateTime.now().minusHours(1);


    private final CaptchaRepository captchaRepository;

    public AuthCaptchaResponse getCaptcha() {
        deleteOldCaptcha();
        String secretCode = CaptchaUtil.generateRandomString();
        Captcha captcha = CaptchaUtil.createCaptcha(captchaOriginWidth, captchaOriginHeight);
        AuthCaptchaResponse authCaptchaResponse = new AuthCaptchaResponse()
                .setSecret(secretCode)
                .setImage(imageEncodeSuffix + CaptchaUtil.encodeCaptcha(captcha, captchaNewWidth, captchaNewHeight));
        CaptchaCode captchaCode = new CaptchaCode()
                .setCode(captcha.getAnswer())
                .setSecretCode(secretCode)
                .setTime(LocalDateTime.now());
        captchaRepository.saveAndFlush(captchaCode);

        return authCaptchaResponse;
    }

    private void deleteOldCaptcha() {
        List<CaptchaCode> captchaCodes = captchaRepository.findLastUpdating();

        for (CaptchaCode cc : captchaCodes
        ) {
            if (cc.getTime().isBefore(timeToDeleteOldCaptcha)) {
                captchaRepository.delete(cc);
            }
        }
    }

}
