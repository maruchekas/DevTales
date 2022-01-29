package org.skillbox.devtales.config;

public final class Constants {

    public static final int ANNOUNCE_LENGTH_LIMIT = 150;
    public static final int CAPTCHA_WIDTH_ORIGIN = 150;
    public static final int CAPTCHA_HEIGHT_ORIGIN = 50;
    public static final int CAPTCHA_WIDTH_NEW = 100;
    public static final int CAPTCHA_HEIGHT_NEW = 35;
    public static final int CAPTCHA_LIFESPAN_BY_SEC = 3600;
    public static final String CAPTCHA_IMAGE_PREFIX = "data:captchaImage/png;base64, ";
    public static final String BASE_ROBOTIC_AVA_URL = "https://robohash.org/";
    public static final String  AVATAR_CONFIG = ".png?size=360x360&set=set";
    public static final String  MAIL_MASSAGE_BASE_URL = "http://localhost:8080/login/change-password/";
    public static final int TARGET_IMAGE_WIDTH_TO_UPLOAD = 360;
    public static final int TARGET_AVATAR_WIDTH_TO_UPLOAD = 56;


    private Constants() {
    }
}
