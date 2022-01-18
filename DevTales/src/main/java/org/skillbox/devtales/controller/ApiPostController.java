package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.ModeratePostRequest;
import org.skillbox.devtales.api.request.PostRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getOnePostById(@PathVariable(value = "id") int id, Principal principal) {
        return ResponseEntity.ok(postService.getPostById(id, principal));
    }

    @GetMapping("/post")
    public ResponseEntity<PostResponse> getPageOfPosts(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            @RequestParam(defaultValue = "recent") String mode) {

        return new ResponseEntity<>(postService.getPosts(offset, limit, mode), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/post/search")
    public ResponseEntity<PostResponse> getPostsByText(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String query) {

        return new ResponseEntity<>(postService.searchPostsByText(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping("/post/byTag")
    public ResponseEntity<PostResponse> getPostsByTag(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String tag) {

        return new ResponseEntity<>(postService.searchPostsByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/post/byDate")
    public ResponseEntity<PostResponse> getPostsByDate(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String date) {
        return new ResponseEntity<>(postService.searchPostsByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/post/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<PostResponse> getPostsForModeration(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String status) {
        return new ResponseEntity<>(postService.getPostsForModeration(offset, limit, status), HttpStatus.OK);
    }

    @GetMapping("/post/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostResponse> getMyPosts(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String status,
            Principal principal) {
        return new ResponseEntity<>(postService.getMyPosts(offset, limit, status, principal), HttpStatus.OK);
    }

    @PostMapping("/post")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommonResponse> addPost(@RequestBody PostRequest postRequest, Principal principal){

        return new ResponseEntity<>(postService.addPost(postRequest, principal), HttpStatus.OK);
    }

    @PutMapping("/post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommonResponse> editPostById(@PathVariable(value = "id") int id,
                                                       @RequestBody PostRequest postRequest,
                                                       Principal principal) {
        return new ResponseEntity<>(postService.editPost(id, postRequest, principal), HttpStatus.OK);
    }

}