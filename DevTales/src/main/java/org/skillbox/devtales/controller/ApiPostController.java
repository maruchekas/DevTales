package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.PostRequest;
import org.skillbox.devtales.api.request.VoteRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.service.PostService;
import org.skillbox.devtales.service.VoteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.skillbox.devtales.config.Constants.NEGATIVE_VOTE;
import static org.skillbox.devtales.config.Constants.POSITIVE_VOTE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;
    private final VoteService voteService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getOnePostById(@PathVariable(value = "id") int id, Principal principal) {
        return ResponseEntity.ok(postService.getPostDtoById(id, principal));
    }

    @GetMapping("")
    public ResponseEntity<PostResponse> getPageOfPosts(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            @RequestParam(defaultValue = "recent") String mode) {

        return new ResponseEntity<>(postService.getPosts(offset, limit, mode), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PostResponse> getPostsByText(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String query) {

        return new ResponseEntity<>(postService.searchPostsByText(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostResponse> getPostsByTag(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String tag) {

        return new ResponseEntity<>(postService.searchPostsByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostResponse> getPostsByDate(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String date) {
        return new ResponseEntity<>(postService.searchPostsByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<PostResponse> getPostsForModeration(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String status) {
        return new ResponseEntity<>(postService.getPostsForModeration(offset, limit, status), HttpStatus.OK);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostResponse> getMyPosts(
            @RequestParam(defaultValue = "offset") int offset,
            @RequestParam(defaultValue = "limit") int limit,
            String status,
            Principal principal) {
        return new ResponseEntity<>(postService.getMyPosts(offset, limit, status, principal), HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommonResponse> addPost(@RequestBody PostRequest postRequest, Principal principal){

        return new ResponseEntity<>(postService.addPost(postRequest, principal), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommonResponse> editPostById(@PathVariable(value = "id") int id,
                                                       @RequestBody PostRequest postRequest,
                                                       Principal principal) {
        return new ResponseEntity<>(postService.editPost(id, postRequest, principal), HttpStatus.OK);
    }

    @PostMapping("/like")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommonResponse> putLike(@RequestBody VoteRequest voteRequest, Principal principal){
        return new ResponseEntity<>(voteService.castVote(voteRequest, POSITIVE_VOTE, principal), HttpStatus.OK);
    }

    @PostMapping("/dislike")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommonResponse> putDislike(@RequestBody VoteRequest voteRequest, Principal principal){
        return new ResponseEntity<>(voteService.castVote(voteRequest, NEGATIVE_VOTE, principal), HttpStatus.OK);
    }

}