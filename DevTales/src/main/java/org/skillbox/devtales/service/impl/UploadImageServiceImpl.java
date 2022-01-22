package org.skillbox.devtales.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.config.Constants;
import org.skillbox.devtales.service.UploadImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class UploadImageServiceImpl implements UploadImageService {

    private final Cloudinary cloudinary;

    public ResponseEntity<?> saveImage(MultipartFile image, Principal principal) throws IOException {
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> errors = validateImageRequest(image);

        if (errors.size() > 0) {
            commonResponse.setResult(false);
            commonResponse.setErrors(errors);

            return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
        }

        Map params = ObjectUtils.asMap(
                "public_id", getUploadedFilePath() + principal.getName());
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), params);

        return new ResponseEntity<>(uploadResult.get("url").toString(), HttpStatus.OK);
    }

    /**
     * метод генерирует изображение при помощи сервиса Robohash и сохраняет его в хранилище Cloudinary
     * при успешной регистрации пользователя, ему, в качестве аватара, устанавливается сгенерированное изображение
     *
     * @param username
     * @return String url - путь к созданному изображению
     * @throws IOException
     */
    public String createAndSaveDefaultAvatarForUser(String username) throws IOException {
        String format = "png";

        String path = getUrlForDefaultAvatar(username);
        BufferedImage image = ImageIO.read(new URL(path));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        byte[] bytes = baos.toByteArray();

        Map params = getUploadImageParams(image, Constants.TARGET_AVATAR_WIDTH_TO_UPLOAD, username);
        Map uploadResult = cloudinary.uploader().upload(bytes, params);

        return uploadResult.get("url").toString();
    }

    private String getUrlForDefaultAvatar(String username) {
        int setNum = new Random().nextInt(4) + 1;
        String randomString = RandomStringUtils.randomAlphanumeric(7);

        return Constants.BASE_ROBOTIC_AVA_URL + username + randomString + Constants.AVATAR_CONFIG + setNum;
    }

    private String getUploadedFilePath() {
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(12);
        String chainRandomFolders = "";

        for (int i = 0; i <= randomAlphanumeric.length(); i += 5) {
            chainRandomFolders = (new StringBuilder(randomAlphanumeric)).insert(i, '/').toString();
            randomAlphanumeric = chainRandomFolders;
        }

        return "upload" + chainRandomFolders;
    }

    private Map getUploadImageParams(BufferedImage image, int targetImgWidth, String username) {
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        int targetImgHeight = (int) Math.round(imageHeight / (imageWidth / (double) targetImgWidth));

        return ObjectUtils.asMap(
                "public_id", getUploadedFilePath() + username,
                "transformation", new Transformation<>().width(targetImgWidth).height(targetImgHeight)
        );
    }

    private Map<String, String> validateImageRequest(MultipartFile image) {
        final long fileSize = image.getSize();
        final String type = image.getContentType();
        Map<String, String> errors = new HashMap<>();

        if (fileSize > 1_572_864) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }

        if (fileSize == 0) {
            errors.put("image", "Размер файла слишклм мал или файл не добавлен");
        }

        assert type != null;
        if (!type.startsWith("image/")) {
            errors.put("content_type", "Неизвестный тип файла");
        }

        return errors;
    }
}
