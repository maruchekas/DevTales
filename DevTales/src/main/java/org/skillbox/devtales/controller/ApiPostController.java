package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.service.impl.PostServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiPostController {

    private final static int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private final PostServiceImpl postServiceImpl;

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getOnePostById(@PathVariable(value = "id") int id) {
        return ResponseEntity.ok(postServiceImpl.getPostById(id));
    }

    @GetMapping("/post")
    public ResponseEntity<PostResponse> getPageOfPosts(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            @RequestParam(defaultValue = "recent") String mode) {

        PostResponse response = postServiceImpl.getPosts(offset, limit, mode);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/post/search")
    public ResponseEntity<PostResponse> getPostsByText(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String query) {
        PostResponse response = postServiceImpl.searchPostsByText(offset, limit, query);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/post/byTag")
    public ResponseEntity<PostResponse> getPostsByTag(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String tag) {
        PostResponse response = postServiceImpl.searchPostsByTag(offset, limit, tag);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/post/byDate")
    public ResponseEntity<PostResponse> getPostsByDate(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String date){
        return new ResponseEntity<>(postServiceImpl.searchPostsByDate(offset, limit, date), HttpStatus.OK);
    }

}