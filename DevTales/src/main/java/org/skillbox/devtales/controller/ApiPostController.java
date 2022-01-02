package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.service.PostServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiPostController {

    private final PostServiceImpl postServiceImpl;

    @GetMapping("/post/{id}")
    public PostDto getOnePostById(@PathVariable(value = "id") int id) {
        return postServiceImpl.getOnePostById(id);
    }

    @GetMapping("/post")
    public ResponseEntity<PostResponse> getPageOfPosts(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "offset") int limit,
            @RequestParam(defaultValue = "recent") String mode) {

        PostResponse response = postServiceImpl.getAllPosts(offset, limit, mode);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

}