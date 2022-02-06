package org.skillbox.devtales.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.config.Constants;
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
import java.util.regex.Pattern;

import static org.skillbox.devtales.config.Constants.*;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class UploadImageService {

    private final Cloudinary cloudinary;

    public ResponseEntity<?> saveImage(MultipartFile image, Principal principal) throws IOException {
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> errors = validateImageRequest(image);

        if (errors.size() > 0) {
            commonResponse.setResult(false);
            commonResponse.setErrors(errors);

            return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(uploadImgToCloudAndGetUrl(image, principal), HttpStatus.OK);
    }

    public String saveCustomAvatarForUser(MultipartFile image, Principal principal) throws IOException {
        BufferedImage bufImage = ImageIO.read(image.getInputStream());
        Map params = getUploadImageParams(bufImage, Constants.TARGET_AVATAR_WIDTH_TO_UPLOAD, principal.getName());
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), params);

        return uploadResult.get("url").toString();
    }

    public String uploadImgToCloudAndGetUrl(MultipartFile image, Principal principal) throws IOException {
        Map params = ObjectUtils.asMap(
                "public_id", getUploadedFilePath() + principal.getName());
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), params);

        return uploadResult.get("url").toString();
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

        if (fileSize > IMG_SIZE_LIMIT) {
            errors.put(IMAGE_ERR, IMAGE_OVERSIZE_ANSWER);
        }

        if (fileSize == 0) {
            errors.put(IMAGE_ERR, IMAGE_NULLSIZE_ANSWER);
        }

        assert type != null;
        if (!Pattern.matches("image/(jpg|png|jpeg)", type)) {
            errors.put(CONTENT_TYPE_ERR, CONTENT_TYPE_ANSWER);
        }

        return errors;
    }
}
