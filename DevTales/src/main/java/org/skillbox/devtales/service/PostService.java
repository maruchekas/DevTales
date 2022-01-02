package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.PostResponse;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

    PostResponse getAllPosts();

    PostResponse getPostById(int id);

    PostResponse getRecentPostsSortedByDate();

    PostResponse getPopularPostsSortedByComment();

    PostResponse getBestPostsSortedByLike();

    PostResponse getEarlyPostsSortedByDate();

}
