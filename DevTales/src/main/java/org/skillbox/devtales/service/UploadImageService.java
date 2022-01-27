package org.skillbox.devtales.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
public interface UploadImageService {

    String createAndSaveDefaultAvatarForUser(String username) throws IOException;

    String saveCustomAvatarForUser(MultipartFile image, Principal principal) throws IOException;

    ResponseEntity<?> saveImage(MultipartFile image, Principal principal) throws IOException;

    String uploadImgToCloudAndGetUrl(MultipartFile image, Principal principal) throws IOException;
}
