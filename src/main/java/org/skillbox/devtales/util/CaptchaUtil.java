package org.skillbox.devtales.util;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class CaptchaUtil {

    public static String generateRandomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String encodeCaptcha(Captcha captcha, int width, int height) {
        String image = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BufferedImage bim = Scalr.resize(captcha.getImage(), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
                    width, height, Scalr.OP_ANTIALIAS);
            ImageIO.write(bim, "png", bos);
            byte[] byteArray = Base64.getEncoder().encode(bos.toByteArray());
            image = new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Captcha createCaptcha(Integer width, Integer height) {
        return new Captcha.Builder(width, height)
                .addBackground(new GradiatedBackgroundProducer(Color.BLUE, Color.WHITE))
                .addText(new DefaultTextProducer(), new DefaultWordRenderer())
                .addNoise(new CurvedLineNoiseProducer())
                .build();
    }

}
