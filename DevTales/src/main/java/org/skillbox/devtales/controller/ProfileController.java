package org.skillbox.devtales.controller;

import lombok.RequiredArgsConstructor;
import org.skillbox.devtales.api.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/profile")
public class ProfileController {

    @PostMapping("/my")
    public ResponseEntity<CommonResponse> editProfile(@RequestParam(value = "photo") MultipartFile multipartFile,
                                                      @RequestParam(value = "removePhoto") int removePhoto){
        return  new ResponseEntity<>(new CommonResponse(), HttpStatus.OK);
    }
}
