package org.skillbox.devtales.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.skillbox.devtales.config.Constants;
import org.skillbox.devtales.service.UploadImageService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class UploadImageServiceImpl implements UploadImageService {

    private final Cloudinary cloudinary;

    public String saveImage(MultipartFile image, Principal principal) throws IOException {

        Map params = getUploadParamsMap(principal.getName(), 1024);
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), params);

        return uploadResult.get("url").toString();
    }

    public String uploadImage(String username) throws IOException {
        String format = "png";

        Map params = getUploadParamsMap(username, 360);
        String path = getUrlForDefaultAvatar(username);
        BufferedImage image = ImageIO.read(new URL(path));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        byte[] bytes = baos.toByteArray();

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

    private Map getUploadParamsMap(String username, int transformationParams) {
        return ObjectUtils.asMap(
                "public_id", getUploadedFilePath() + username,
                "transformation", new Transformation<>().width(transformationParams).height(transformationParams)
        );
    }
}
