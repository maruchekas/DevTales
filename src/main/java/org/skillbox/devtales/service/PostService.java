package org.skillbox.devtales.service;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.request.ModeratePostRequest;
import org.skillbox.devtales.api.request.PostRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.api.response.UserDataResponse;
import org.skillbox.devtales.config.Constants;
import org.skillbox.devtales.dto.PostCommentDto;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.exception.PostNotFoundException;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.PostComment;
import org.skillbox.devtales.model.Tag;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.model.enums.ModerationStatus;
import org.skillbox.devtales.repository.*;
import org.skillbox.devtales.util.DateTimeUtil;
import org.skillbox.devtales.util.HtmlToSimpleTextUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static org.skillbox.devtales.config.Constants.POST_NOT_FOUND;

@Component
@AllArgsConstructor
public class PostService {

    private final AuthUserService userService;
    private final SettingsService settingsService;
    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

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

    public PostDto getPostDtoById(int id, Principal principal) {
        Post post = principal == null ? postRepository.findPostById(id).orElseThrow(() ->
                new PostNotFoundException(String.format(POST_NOT_FOUND, id)))
                : postRepository.findAnyPostById(id).orElseThrow(() ->
                new PostNotFoundException(String.format(POST_NOT_FOUND, id)));
        if (principal == null ||
                !(userService.getUserByEmail(principal.getName()).getIsModerator() == 1
                        || userService.getUserByEmail(principal.getName()).getId() == post.getUser().getId())) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        }

        PostDto postView = getPostData(post);
        postView.setAnnounce(null);
        postView.setComments(getCommentsForPost(post));
        postView.setTags(getTagsFromPost(post));

        return postView;
    }

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

    public PostResponse getMyPosts(int offset, int limit, String status, Principal principal) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts;
        String inStatus;
        byte isActive;
        User user = userService.getUserByEmail(principal.getName());

        switch (status) {
            case "pending" -> {
                inStatus = "NEW";
                isActive = 1;
            }
            case "declined" -> {
                inStatus = "DECLINED";
                isActive = 1;
            }
            case "published" -> {
                inStatus = "ACCEPTED";
                isActive = 1;
            }
            default -> {
                inStatus = "NEW";
                isActive = 0;
            }
        }
        pagePosts = postRepository.findMyPosts(inStatus, user.getId(), isActive, pageable);

        return getPostResponseFromPostsPage(pagePosts);
    }

    public CommonResponse addPost(PostRequest postRequest, Principal principal) {

        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> errors = validateAddPostRequest(postRequest);

        if (errors.size() > 0) {
            commonResponse.setResult(false);
            commonResponse.setErrors(errors);

            return commonResponse;
        }

        Set<Tag> tags = addTagsToPost(postRequest.getTags());
        commonResponse.setResult(true);
        Post post = new Post()
                .setTitle(postRequest.getTitle())
                .setText(postRequest.getText())
                .setIsActive(postRequest.getActive())
                .setDateTime(getCorrectPostTime(postRequest.getTimestamp()))
                .setUser(userRepository.findByEmail(principal.getName()).orElseThrow())
                .setViewCount(0)
                .setModerationStatus(getCorrectModerationStatus());
        post.setTags(tags);
        postRepository.save(post);

        return commonResponse;
    }

    public CommonResponse editPost(int id, PostRequest postRequest, Principal principal) {
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> errors = validateAddPostRequest(postRequest);
        Post post = postRepository.findAnyPostById(id).orElseThrow(
                () -> new PostNotFoundException(POST_NOT_FOUND));

        if (errors.size() > 0) {
            commonResponse.setResult(false);
            commonResponse.setErrors(errors);

            return commonResponse;
        }

        post.setDateTime(getCorrectPostTime(postRequest.getTimestamp()));
        post.setModerationStatus(getModerationStatusForEditedPost(principal.getName(), post.getModerationStatus()));

        if (postRequest.getTitle() != null) {
            post.setTitle(postRequest.getTitle());
        }
        if (postRequest.getText() != null) {
            post.setText(postRequest.getText());
        }
        if (postRequest.getTags().length != 0) {
            Set<Tag> tags = addTagsToPost(postRequest.getTags());
            post.setTags(tags);
        }
        post.setIsActive(postRequest.getActive());
        commonResponse.setResult(true);
        postRepository.save(post);

        return commonResponse;
    }

    public CommonResponse moderatePost(ModeratePostRequest moderatePostRequest, Principal principal) {
        CommonResponse commonResponse = new CommonResponse();
        int postId = moderatePostRequest.getPostId();
        String decision = moderatePostRequest.getDecision();

        Post post = postRepository.findAnyPostById(postId).orElseThrow(
                () -> new PostNotFoundException(POST_NOT_FOUND));
        User moderator = userService.getUserByEmail(principal.getName());

        if (moderator.getIsModerator() == 1) {
            switch (decision) {
                case "accept" -> {
                    post.setModerationStatus(ModerationStatus.ACCEPTED);
                    post.setModerator(moderator);
                }
                case "decline" -> {
                    post.setModerationStatus(ModerationStatus.DECLINED);
                    post.setModerator(moderator);
                }
                default -> post.setModerationStatus(post.getModerationStatus());
            }
            postRepository.save(post);
            commonResponse.setResult(true);
            return commonResponse;
        }

        return commonResponse;
    }

    private Map<String, String> validateAddPostRequest(PostRequest postRequest) {
        final String title = postRequest.getTitle();
        final String text = postRequest.getText();
        final String simplePostText = HtmlToSimpleTextUtil.getSimpleTextFromHtml(text, text.length());
        Map<String, String> errors = new HashMap<>();

        if (title.length() < 3) {
            errors.put("title", "Заголовок не установлен");
        }

        if (simplePostText.length() < 50) {
            errors.put("text", "Текст публикации слишком короткий");
        }

        return errors;
    }

    public Set<Tag> addTagsToPost(String[] tagsString) {
        Set<Tag> tags = new HashSet<>();
        for (String tagFromNewPost : tagsString
        ) {
            Tag tag = tagRepository.findByName(tagFromNewPost).orElseGet(() -> createNewTag(tagFromNewPost));
            tags.add(tag);
        }
        return tags;
    }

    private Tag createNewTag(String tagName) {
        Tag newTag = new Tag().setName(tagName);
        tagRepository.save(newTag);
        return newTag;
    }

    private PostDto getPostData(Post post) {
        PostDto postData = new PostDto()
                .setId(post.getId())
                .setTimestamp(DateTimeUtil.getTimestamp(post.getDateTime()))
                .setUser(getUserDataForPost(post.getUser()))
                .setTitle(post.getTitle())
                .setText(post.getText())
                .setAnnounce(getAnnounce(post.getText()))
                .setCommentCount(post.getComments().size())
                .setLikeCount(getLikeCount(post))
                .setDislikeCount(getDislikeCount(post))
                .setViewCount(post.getViewCount());

        postData.setIsActive(post.getIsActive());

        return postData;
    }

    public Post getPostById(int id){
        return postRepository.findAnyPostById(id).orElseThrow(
                () -> new PostNotFoundException("Post with id " + id + " not found"));
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
        String announceText = HtmlToSimpleTextUtil.getSimpleTextFromHtml(text, text.length());
        return announceText.length() > Constants.ANNOUNCE_LENGTH_LIMIT ?
                announceText.substring(0, Constants.ANNOUNCE_LENGTH_LIMIT) + "..."
                : announceText;
    }

    private LocalDateTime getCorrectPostTime(long timestamp) {

        return DateTimeUtil.getLocalDateTime(timestamp).isBefore(LocalDateTime.now())
                ? LocalDateTime.now()
                : DateTimeUtil.getLocalDateTime(timestamp);
    }

    private ModerationStatus getCorrectModerationStatus(){
        return settingsService.getGlobalSettings().isPostPremoderation()
                ? ModerationStatus.NEW
                : ModerationStatus.ACCEPTED;
    }

    private ModerationStatus getModerationStatusForEditedPost(String userEmail, ModerationStatus currentModerationStatus) {
        return userService.getUserByEmail(userEmail).getIsModerator() == 1
                ? currentModerationStatus
                : getCorrectModerationStatus();
    }

    private List<PostCommentDto> getCommentsForPost(Post post) {
        List<PostComment> comments = commentRepository.findPostCommentsByPostId(post.getId());
        List<PostCommentDto> commentsDto = new ArrayList<>();

        for (PostComment comment : comments
        ) {
            commentsDto.add(getCommentData(comment));
        }

        return commentsDto;
    }

    private Set<String> getTagsFromPost(Post post) {
        Set<Tag> tags = post.getTags();
        Set<String> tagsName = new HashSet<>();

        for (Tag tag : tags
        ) {
            tagsName.add(tag.getName());
        }

        return tagsName;
    }

    private PostCommentDto getCommentData(PostComment comment) {

        return new PostCommentDto()
                .setId(comment.getId())
                .setTimestamp(DateTimeUtil.getTimestamp(comment.getTime()))
                .setText(comment.getText())
                .setUser(getUserDataForComment(comment.getUser()));
    }

    private UserDataResponse getUserDataForComment(User user) {

        return new UserDataResponse()
                .setId(user.getId())
                .setName(user.getName())
                .setPhoto(user.getPhoto());
    }

    private UserDataResponse getUserDataForPost(User user) {

        return new UserDataResponse()
                .setId(user.getId())
                .setName(user.getName());
    }

    private int getLikeCount(Post post) {
        return postVoteRepository.findCountLikesOfPostById(post.getId()).orElse(0);
    }

    private int getDislikeCount(Post post) {
        return postVoteRepository.findCountDislikesOfPostById(post.getId()).orElse(0);
    }
}
