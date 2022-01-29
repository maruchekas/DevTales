package org.skillbox.devtales.service;

import org.skillbox.devtales.api.request.ModeratePostRequest;
import org.skillbox.devtales.api.request.PostRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.exception.PostNotFoundException;
import org.skillbox.devtales.model.Post;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface PostService {

    PostResponse getPosts(int offset, int limit, String mode);

    PostDto getPostDtoById(int id, Principal principal) throws PostNotFoundException;

    Post getPostById(int id);

    PostResponse searchPostsByTag(int offset, int limit, String tag);

    PostResponse searchPostsByText(int offset, int limit, String text);

    PostResponse searchPostsByDate(int offset, int limit, String date);

    PostResponse getPostsForModeration(int offset, int limit, String status);

    PostResponse getMyPosts(int offset, int limit, String status, Principal principal);

    CommonResponse addPost(PostRequest postRequest, Principal principal);

    CommonResponse editPost(int id, PostRequest postRequest, Principal principal);

    CommonResponse moderatePost(ModeratePostRequest moderatePostRequest, Principal principal);

}
