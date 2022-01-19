package org.skillbox.devtales.service.impl;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.PostCommentRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.api.response.ParentResponse;
import org.skillbox.devtales.api.response.PostCommentResponse;
import org.skillbox.devtales.exception.CommentNotFoundException;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.PostComment;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.CommentRepository;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.PostCommentService;
import org.skillbox.devtales.util.HtmlToSimpleTextUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    PostRepository postRepository;
    CommentRepository commentRepository;
    UserRepository userRepository;

    public ParentResponse addCommentToPost(PostCommentRequest postCommentRequest, Principal principal){
        PostComment postComment = new PostComment();
        int parentId = postCommentRequest.getParentId();
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> errors = validateAddPostCommentRequest(postCommentRequest);

        if (parentId != 0){
        PostComment parent = getPostCommentById(parentId);
        postComment.setParent(parent);
        }

        if (errors.size() > 0) {
            commonResponse.setResult(false);
            commonResponse.setErrors(errors);

            return commonResponse;
        }

        postComment.setPost(getPostById(postCommentRequest.getPostId()));
        postComment.setText(postCommentRequest.getText());
        postComment.setUser(getUserByEmail(principal.getName()));
        postComment.setTime(LocalDateTime.now());

        commentRepository.save(postComment);

        return new PostCommentResponse()
        .setId(postComment.getId());
    }

    private Map<String, String> validateAddPostCommentRequest(PostCommentRequest postCommentRequest){
        final String text = postCommentRequest.getText();
        final String simplePostText = HtmlToSimpleTextUtil.getSimpleTextFromHtml(text, text.length());
        Map<String, String> errors = new HashMap<>();

        if (simplePostText.length() < 20) {
            errors.put("text", "Текст комментария не задан или слишком короткий");
        }

        return errors;
    }

    private PostComment getPostCommentById(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));
    }

    private Post getPostById(int id) {
        return postRepository.findPostById(id)
                .orElseThrow(() -> new CommentNotFoundException("Post with id " + id + " not found or inactive"));
    }

    private User getUserByEmail(String userName) {
        return userRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userName + " not found"));
    }

}
