package org.skillbox.devtales.service;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.PostCommentRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.api.response.ParentResponse;
import org.skillbox.devtales.api.response.PostCommentResponse;
import org.skillbox.devtales.exception.ElementNotFoundException;
import org.skillbox.devtales.exception.UnAuthorisedUserException;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.PostComment;
import org.skillbox.devtales.repository.CommentRepository;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.util.HtmlToSimpleTextUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.skillbox.devtales.config.Constants.*;

@Component
@AllArgsConstructor
public class PostCommentService {

    AuthUserService userService;
    PostRepository postRepository;
    CommentRepository commentRepository;
    UserRepository userRepository;

    public ResponseEntity<ParentResponse> addCommentToPost(PostCommentRequest postCommentRequest, Principal principal) throws UnAuthorisedUserException {

        if (principal == null) {
            throw new UnAuthorisedUserException();
        }
        PostComment postComment = new PostComment();
        int parentId = postCommentRequest.getParentId();
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> errors = validateAddPostCommentRequest(postCommentRequest);

        if (parentId != 0) {
            PostComment parent = getPostCommentById(parentId);
            postComment.setParent(parent);
        }

        if (errors.size() > 0) {
            commonResponse.setResult(false);
            commonResponse.setErrors(errors);

            return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
        }

        postComment.setPost(getPostById(postCommentRequest.getPostId()));
        postComment.setText(postCommentRequest.getText());
        postComment.setUser(userService.getUserByEmail(principal.getName()));
        postComment.setTime(LocalDateTime.now());

        commentRepository.save(postComment);

        return new ResponseEntity<>(new PostCommentResponse().setId(postComment.getId()), HttpStatus.OK);
    }

    private Map<String, String> validateAddPostCommentRequest(PostCommentRequest postCommentRequest) {
        final String text = postCommentRequest.getText();
        final String simplePostText = HtmlToSimpleTextUtil.getSimpleTextFromHtml(text, text.length());
        Map<String, String> errors = new HashMap<>();

        if (simplePostText.length() < 20) {
            errors.put(TEXT_ERR, TEXT_COMMENT_ANSWER);
        }

        return errors;
    }

    private PostComment getPostCommentById(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(COMMENT_NOT_FOUND, id)));
    }

    private Post getPostById(int id) {
        return postRepository.findPostById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(POST_NOT_FOUND, id)));
    }

}
