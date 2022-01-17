package org.skillbox.devtales.service.impl;


import cn.apiclub.captcha.Captcha;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.api.response.AuthCaptchaResponse;
import org.skillbox.devtales.config.Constants;
import org.skillbox.devtales.model.CaptchaCode;
import org.skillbox.devtales.repository.CaptchaRepository;
import org.skillbox.devtales.service.CaptchaService;
import org.skillbox.devtales.util.CaptchaUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Data
@Setter
@Getter
public class CaptchaServiceImpl implements CaptchaService {

    private static LocalDateTime timeToDeleteOldCaptcha =
            LocalDateTime.now().minusSeconds(Constants.CAPTCHA_LIFESPAN_BY_SEC);

    private final CaptchaRepository captchaRepository;

    public AuthCaptchaResponse getCaptcha() {
        deleteOldCaptcha();
        String secretCode = CaptchaUtil.generateRandomString();
        Captcha captcha = CaptchaUtil.createCaptcha(Constants.CAPTCHA_WIDTH_ORIGIN, Constants.CAPTCHA_HEIGHT_ORIGIN);
        AuthCaptchaResponse authCaptchaResponse = new AuthCaptchaResponse()
                .setSecret(secretCode)
                .setImage(Constants.CAPTCHA_IMAGE_PREFIX +
                        CaptchaUtil.encodeCaptcha(captcha, Constants.CAPTCHA_WIDTH_NEW, Constants.CAPTCHA_HEIGHT_NEW));
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
