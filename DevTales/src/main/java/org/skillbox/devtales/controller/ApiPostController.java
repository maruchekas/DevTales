package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.dto.UserDto;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.PostVoteRepository;
import org.skillbox.devtales.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@AllArgsConstructor
@RequestMapping
public class ApiPostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostResponse postResponse;

    @Autowired
    PostVoteRepository postVoteRepository;

    ModelMapper modelMapper;

    private final PostService postService;

    @GetMapping("/api/post/{id}")
    public PostDto getOnePostById(@PathVariable(value = "id") int id) {
        return postService.getOnePostById(id);
    }

    @GetMapping("/api/post")
    public ResponseEntity<PostResponse> getPageOfPosts(
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "dateTime") String sortBy,
            @RequestParam(defaultValue = "recent") String mode) {
        switch (mode) {
            case "recent":
                sortBy = "dateTime";
                break;
            case "popular":
                sortBy = "viewCount";
                break;
            case "best":
                sortBy = "title";
                break;
        }
        PostResponse response = postService.getAllPosts(offset, limit, sortBy, mode);
        System.out.println(offset + " " + limit + " " + sortBy);

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }


    private PostDto convertPostToDto(Post post) {
        PostDto postDto = modelMapper.map(post, PostDto.class);
        if (post.getText().length() > 150) {
            String announce = post.getText().substring(0, 150) + "...";
            postDto.setAnnounce(announce);
        } else {
            postDto.setAnnounce(post.getText());
        }

        postDto.setTimestamp(Timestamp.valueOf(post.getDateTime()).getTime() / 1000 - 10800);
        postDto.setViewCount(postDto.getViewCount());
        postDto.setCommentCount(post.getComments().size());
        postDto.setLikeCount(getLikeCount(post));
        postDto.setDislikeCount(getDislikeCount(post));
        postDto.setUser(convertUserToDto(post.getUser()));

        return postDto;
    }

    private UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private int getLikeCount(Post post) {
        return postVoteRepository.findCountLikesOfPostById(post.getId());
    }

    private int getDislikeCount(Post post) {
        return postVoteRepository.findCountDislikesOfPostById(post.getId());
    }

}
