package org.skillbox.devtales.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiPostController {

  private final PostService postService;

//  @GetMapping("/post/{id}")
//  public PostResponse getOnePostById(){
//    return postService.getPost();
//  }

  @GetMapping("/post")
  public ResponseEntity<List<Post>> getAllEmployees(
      @RequestParam(defaultValue = "0") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "moderationStatus") String sortBy)
  {
    List<Post> list = postService.getAllPosts(pageNo, pageSize, sortBy);

    return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
  }

}
