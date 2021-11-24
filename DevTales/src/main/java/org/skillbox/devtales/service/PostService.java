package org.skillbox.devtales.service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

  PostRepository postRepository;

  public List<Post> getAllPosts(Integer pageNo, Integer pageSize, String sortBy) {

    Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

    Page<Post> pageResult = postRepository.findAll(paging);

    if (pageResult.hasContent()) {
      return pageResult.getContent();
    } else {
      return new ArrayList<>();
    }

  }
}
