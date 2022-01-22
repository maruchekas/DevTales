package org.skillbox.devtales.service;

import org.skillbox.devtales.api.request.PostCommentRequest;
import org.skillbox.devtales.api.response.ParentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface PostCommentService {

    ResponseEntity<ParentResponse> addCommentToPost(PostCommentRequest postCommentRequest, Principal principal);
}
