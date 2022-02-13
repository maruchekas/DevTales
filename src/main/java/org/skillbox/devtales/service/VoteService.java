package org.skillbox.devtales.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.skillbox.devtales.api.request.VoteRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.PostVote;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.PostVoteRepository;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class VoteService {

    private final PostVoteRepository postVoteRepository;
    private final PostService postService;
    private final AuthUserService userService;

    public CommonResponse castVote(VoteRequest voteRequest, int value, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Post post = postService.getPostById(voteRequest.getPostId());
        PostVote postVote = postVoteRepository.findPostVoteByPostAndUser(post, user)
                .orElse(new PostVote()
                        .setPost(post)
                        .setUser(user)
                        .setTime(LocalDateTime.now()));
        if (postVote.getValue() == value) {
            return new CommonResponse().setResult(false);
        }
        postVote.setValue(value);
        postVoteRepository.save(postVote);

        return new CommonResponse().setResult(true);
    }
}
