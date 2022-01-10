package org.skillbox.devtales.service.impl;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostCommentDto;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.dto.UserDto;
import org.skillbox.devtales.exception.PostNotFoundException;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.PostComment;
import org.skillbox.devtales.model.Tag;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.PostVoteRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final int MIL_TO_SEC = 1000;
    private final int ANNOUNCE_LENGTH_LIMIT = 150;

    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;
    private final UserRepository userRepository;

    public PostResponse getPosts(int offset, int limit, String mode) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts;

        switch (mode) {
            case "popular" -> pagePosts = postRepository.findPopularPostsSortedByCommentsCount(pageable);
            case "best" -> pagePosts = postRepository.findBestPostsSortedByLikeCount(pageable);
            case "early" -> pagePosts = postRepository.findNewPostsSortedByDate(pageable);
            default -> pagePosts = postRepository.findRecentPostsSortedByDate(pageable);
        }

        return getPostResponseFromPostsPage(pagePosts);
    }

    public PostDto getPostById(int id, Principal principal) {
        Post post = postRepository.findPostById(id).orElseThrow(() ->
                new PostNotFoundException("Пост с id " + id + " не существует или заблокирован"));
        if (principal == null ||
                !(userRepository.findByEmail(principal.getName()).get().getIsModerator() == 1
                        || userRepository.findByEmail(principal.getName()).get().getId() == post.getUser().getId())) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        }

        PostDto postView = getPostData(post);
        postView.setAnnounce(null);
        postView.setComments(getCommentsForPost(post));
        postView.setTags(getTagsForPost(post));

        return postView;
    }

    @Override
    public PostResponse searchPostsByText(int offset, int limit, String text) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts;

        if (text.trim().equals("")) {
            pagePosts = postRepository.findRecentPostsSortedByDate(pageable);
        } else pagePosts = postRepository.findPostsByText(text, pageable);

        return getPostResponseFromPostsPage(pagePosts);
    }

    public PostResponse searchPostsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts;

        if (tag.trim().equals("")) {
            pagePosts = postRepository.findRecentPostsSortedByDate(pageable);
        } else pagePosts = postRepository.findPostsByTags(tag, pageable);

        return getPostResponseFromPostsPage(pagePosts);
    }

    public PostResponse searchPostsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts;

        pagePosts = postRepository.findPostsByDate(date, pageable);

        return getPostResponseFromPostsPage(pagePosts);
    }

    public PostResponse getPostsForModeration(int offset, int limit, String status) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts;

        pagePosts = postRepository.findNewPostsForModeration(status, pageable);

        return getPostResponseFromPostsPage(pagePosts);
    }

    private PostDto getPostData(Post post) {
        PostDto postData = new PostDto();
        postData.setId(post.getId());
        postData.setTimestamp(Timestamp.valueOf(post.getDateTime()).getTime() / MIL_TO_SEC);
        postData.setIsActive(post.getIsActive());
        postData.setUser(getUserData(post.getUser()));
        postData.getUser().setPhoto(null);
        postData.setTitle(post.getTitle());
        postData.setText(post.getText());
        postData.setAnnounce(getAnnounce(post.getText()));
        postData.setCommentCount(post.getComments().size());
        postData.setLikeCount(getLikeCount(post));
        postData.setDislikeCount(getDislikeCount(post));
        postData.setViewCount(post.getViewCount());

        return postData;
    }

    private PostResponse getPostResponseFromPostsPage(Page<Post> pagePosts) {
        PostResponse postResponse = new PostResponse();
        List<PostDto> postsDto = new ArrayList<>();

        for (Post post : pagePosts
        ) {
            postsDto.add(getPostData(post));
        }
        postResponse.setCount((int) pagePosts.getTotalElements());
        postResponse.setPosts(postsDto);

        return postResponse;
    }

    private String getAnnounce(String text) {
        String announce;
        if (text.length() > ANNOUNCE_LENGTH_LIMIT) {
            announce = text.substring(0, ANNOUNCE_LENGTH_LIMIT) + "...";
        } else {
            announce = text;
        }
        return announce;
    }

    private List<PostCommentDto> getCommentsForPost(Post post) {
        List<PostComment> comments = post.getComments();
        List<PostCommentDto> commentsDto = new ArrayList<>();

        for (PostComment comment : comments
        ) {
            commentsDto.add(getCommentData(comment));
        }

        return commentsDto;
    }

    private List<String> getTagsForPost(Post post) {
        List<Tag> tags = post.getTags();
        List<String> tagsName = new ArrayList<>();

        for (Tag tag : tags
        ) {
            tagsName.add(tag.getName());
        }

        return tagsName;
    }

    private PostCommentDto getCommentData(PostComment comment) {
        PostCommentDto commentDto = new PostCommentDto();
        commentDto.setId(comment.getId());
        commentDto.setTimestamp(Timestamp.valueOf(comment.getTime()).getTime() / MIL_TO_SEC);
        commentDto.setText(comment.getText());
        commentDto.setUser(getUserData(comment.getUser()));

        return commentDto;
    }

    private UserDto getUserData(User user) {
        UserDto userData = new UserDto();
        userData.setId(user.getId());
        userData.setName(user.getName());
        userData.setPhoto(user.getPhoto());

        return userData;
    }

    private int getLikeCount(Post post) {
        return postVoteRepository.findCountLikesOfPostById(post.getId());
    }

    private int getDislikeCount(Post post) {
        return postVoteRepository.findCountDislikesOfPostById(post.getId());
    }
}
