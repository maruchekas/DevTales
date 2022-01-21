package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.ImageRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.service.UploadImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class StorageController {

    private final UploadImageService uploadImageService;

    @PostMapping("/image")
    public ResponseEntity<CommonResponse> uploadImage(@RequestBody MultipartFile image, Principal principal) throws IOException {
        CommonResponse commonResponse = new CommonResponse();
        System.out.println(image.getName());
        System.out.println(image.getBytes().length);
        System.out.println(image.getOriginalFilename());
        System.out.println(image.getContentType());
        uploadImageService.saveImage(image, principal);

        commonResponse.setResult(true);

        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
}
