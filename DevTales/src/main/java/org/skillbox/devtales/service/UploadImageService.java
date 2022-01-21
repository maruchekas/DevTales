package org.skillbox.devtales.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
public interface UploadImageService {

    String uploadImage(String imageUrl) throws IOException;

    String saveImage(MultipartFile image, Principal principal) throws IOException;
}
