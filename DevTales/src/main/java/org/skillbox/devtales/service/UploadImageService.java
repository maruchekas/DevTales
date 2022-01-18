package org.skillbox.devtales.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UploadImageService {

    String uploadImage(String imageUrl) throws IOException;
}
