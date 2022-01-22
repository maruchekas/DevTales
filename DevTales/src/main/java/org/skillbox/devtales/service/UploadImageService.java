package org.skillbox.devtales.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
public interface UploadImageService {

    String createAndSaveDefaultAvatarForUser(String imageUrl) throws IOException;

    Object saveImage(MultipartFile image, Principal principal) throws IOException;
}
