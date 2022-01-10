package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.service.impl.PostServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Calendar;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiPostController {

    private final static int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private final PostServiceImpl postServiceImpl;

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getOnePostById(@PathVariable(value = "id") int id, Principal principal) {
        return ResponseEntity.ok(postServiceImpl.getPostById(id, principal));
    }

    @GetMapping("/post")
    public ResponseEntity<PostResponse> getPageOfPosts(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            @RequestParam(defaultValue = "recent") String mode) {

        return new ResponseEntity<>(postServiceImpl.getPosts(offset, limit, mode), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/post/search")
    public ResponseEntity<PostResponse> getPostsByText(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String query) {

        return new ResponseEntity<>(postServiceImpl.searchPostsByText(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping("/post/byTag")
    public ResponseEntity<PostResponse> getPostsByTag(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String tag) {

        return new ResponseEntity<>(postServiceImpl.searchPostsByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/post/byDate")
    public ResponseEntity<PostResponse> getPostsByDate(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String date) {
        return new ResponseEntity<>(postServiceImpl.searchPostsByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/post/moderation")
    public ResponseEntity<PostResponse> getPostsForModeration(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String status) {
        return new ResponseEntity<>(postServiceImpl.getPostsForModeration(offset, limit, status), HttpStatus.OK);
    }

}