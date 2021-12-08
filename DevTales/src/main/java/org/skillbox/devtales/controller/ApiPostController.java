package org.skillbox.devtales.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.dto.UserDto;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiPostController {

  @Autowired
  PostRepository postRepository;

  @Autowired
  PostResponse postResponse;

  private final PostService postService;

  private final ModelMapper modelMapper;

  @GetMapping("/post/{id}")
  public PostDto getOnePostById(@PathVariable(value = "id") int id){
    return postService.getOnePostById(id);
  }

  @GetMapping("/post")
  public ResponseEntity<PostResponse> getPageOfPosts(
          @RequestParam(defaultValue = "0") Integer offset,
          @RequestParam(defaultValue = "12") Integer limit,
          @RequestParam(defaultValue = "dateTime") String sortBy)
  {
    PostResponse response = postService.getAllPosts(offset, limit, sortBy);
    System.out.println(offset + " " + limit + " " + sortBy);

    return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
  }

}
