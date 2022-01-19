package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.PostCommentRequest;
import org.skillbox.devtales.api.response.ParentResponse;
import org.skillbox.devtales.service.PostCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PostCommentController {

    PostCommentService postCommentService;

    @PostMapping("/comment")
    public ResponseEntity<ParentResponse> addComment(@RequestBody PostCommentRequest postCommentRequest, Principal principal){
        return new ResponseEntity<>(postCommentService.addCommentToPost(postCommentRequest, principal), HttpStatus.OK);
    }
}
