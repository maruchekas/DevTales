package org.skillbox.devtales.config;

public final class Constants {

    public static final int ANNOUNCE_LENGTH_LIMIT = 150;
    public static final int ENCODING_STRENGTH = 12;
    public static final int CAPTCHA_WIDTH_ORIGIN = 150;
    public static final int CAPTCHA_HEIGHT_ORIGIN = 50;
    public static final int CAPTCHA_WIDTH_NEW = 100;
    public static final int CAPTCHA_HEIGHT_NEW = 35;
    public static final int CAPTCHA_LIFESPAN_BY_SEC = 3600;
    public static final int IMG_SIZE_LIMIT = 5_242_880;
    public static final String CAPTCHA_IMAGE_PREFIX = "data:captchaImage/png;base64, ";
    public static final String BASE_ROBOTIC_AVA_URL = "https://robohash.org/";
    public static final String AVATAR_CONFIG = ".png?size=360x360&set=set";
    public static final String MAIL_MASSAGE_BASE_URL = "http://localhost:8080/login/change-password/";
    public static final String UPLOAD_FOLDER_PREFIX = "upload";
    public static final String NAME_ERR = "name";
    public static final String USER_ERR = "user";
    public static final String EMAIL_ERR = "email";
    public static final String CAPTCHA_ERR = "captcha";
    public static final String CODE_ERR = "code";
    public static final String PASSWORD_ERR = "password";
    public static final String IMAGE_ERR = "image";
    public static final String CONTENT_TYPE_ERR = "content_type";
    public static final String TEXT_ERR = "text";
    public static final String TITLE_ERR = "title";
    public static final String NAME_ANSWER = "Имя указано неверно";
    public static final String EMAIL_ANSWER = "Этот e-mail уже зарегистрирован";
    public static final String EMAIL_FORMAT_ANSWER = "Неверный формат email";
    public static final String RECOVERY_PASSWORD_MESSAGE_TITLE = "Восстановление пароля для пользователя %s";
    public static final String CAPTCHA_ANSWER = "Код с картинки введён неверно";
    public static final String CONTENT_TYPE_ANSWER = "Неизвестный тип файла";
    public static final String IMAGE_OVERSIZE_ANSWER = "Размер файла превышает допустимый размер";
    public static final String IMAGE_NULLSIZE_ANSWER = "Размер файла слишком мал или файл не добавлен";
    public static final String PASSWORD_ANSWER = "Пароль короче 6-ти символов";
    public static final String CODE_ANSWER = "Ссылка для восстановления пароля устарела. " +
            "<a href=\"/auth/restore\">Запросить ссылку снова</a>";
    public static final String TEXT_ANSWER = "Текст не задан или слишком короткий";
    public static final String TITLE_ANSWER = "Заголовок не установлен или слишком короткий";
    public static final String TEXT_COMMENT_ANSWER = "Текст комментария не задан или слишком короткий";
    public static final String USER_NOT_FOUND = "Пользователь с почтовым ящиком %s не найден";
    public static final String USER_NOT_AUTHORISED = "Пользователь не авторизован";
    public static final String ACCESS_DENIED = "Доступ для пользователя не разрешен";
    public static final String USER_NOT_FOUND_OR_BLOCKED = "Пользователь не существует или заблокирован";
    public static final String COMMENT_NOT_FOUND = "Комментарий с id %d не существует или заблокирован";
    public static final String POST_NOT_FOUND = "Пост с id %d не существует или заблокирован";
    public static final String WRONG_PARAMETER = "Неверно заданы параметры поиска";
    public static final int TARGET_IMAGE_WIDTH_TO_UPLOAD = 360;
    public static final int TARGET_AVATAR_WIDTH_TO_UPLOAD = 56;
    public static final int POSITIVE_VOTE = 1;
    public static final int NEGATIVE_VOTE = -1;

    public static final String NAME_FORMAT_PATTERN = "^[a-zа-яA-ZА-Я0-9 _.-]*$";
    public static final String EMAIL_FORMAT_PATTERN
            = "^[\\w]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";


    private Constants() {
    }
}
