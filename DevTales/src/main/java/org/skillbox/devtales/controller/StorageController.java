package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.service.UploadImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class StorageController {

    private final UploadImageService uploadImageService;

    @PostMapping("/image")
    public ResponseEntity<CommonResponse> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        CommonResponse commonResponse = new CommonResponse();
        System.out.println(image.getName());
        System.out.println(Arrays.toString(image.getBytes()));
        System.out.println(image.getOriginalFilename());
        System.out.println(image.getContentType());

        commonResponse.setResult(true);

        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
}
