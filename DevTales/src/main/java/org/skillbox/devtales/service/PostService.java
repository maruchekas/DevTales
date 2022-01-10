package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.exception.PostNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface PostService {

    PostResponse getPosts(int offset, int limit, String mode);

    PostDto getPostById(int id, Principal principal) throws PostNotFoundException;

    PostResponse searchPostsByText(int offset, int limit, String text);

}
