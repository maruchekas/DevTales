package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.ModeratePostRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ModerateController {

    private final PostService postService;

    @PostMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<CommonResponse> moderatePost(@RequestBody ModeratePostRequest moderatePostRequest, Principal principal){
        return new ResponseEntity<>(postService.moderatePost(moderatePostRequest, principal), HttpStatus.OK);
    }
}
