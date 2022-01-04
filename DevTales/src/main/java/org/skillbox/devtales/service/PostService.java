package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    PostResponse getPosts(int offset, int limit, String mode);

    PostDto getPostById(int id);

    PostResponse searchPostsByText(int offset, int limit, String text);

//    PostResponse getRecentPostsSortedByDate();
//
//    PostResponse getPopularPostsSortedByComment();
//
//    PostResponse getBestPostsSortedByLike();
//
//    PostResponse getEarlyPostsSortedByDate();

}
