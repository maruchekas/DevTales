package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.repository.PostRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiPostController {

  private final PostRepository postRepository;

  @GetMapping("/post")
  public Iterable<Post> post(){
    return postRepository.findAll();
  }

}
