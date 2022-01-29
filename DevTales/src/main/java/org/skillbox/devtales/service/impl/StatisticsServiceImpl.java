package org.skillbox.devtales.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.skillbox.devtales.api.response.StatisticsResponse;
import org.skillbox.devtales.exception.UnAuthorisedUserException;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.PostVoteRepository;
import org.skillbox.devtales.service.AuthUserService;
import org.skillbox.devtales.service.SettingsService;
import org.skillbox.devtales.service.StatisticsService;
import org.skillbox.devtales.util.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class StatisticsServiceImpl implements StatisticsService {

    private final AuthUserService userService;
    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;
    private final SettingsService settingsService;

    @Override
    public StatisticsResponse getMyStatistics(Principal principal) throws UnAuthorisedUserException {
        if (principal == null){
            throw new UnAuthorisedUserException();
        }
        int userId = userService.getUserByEmail(principal.getName()).getId();
        return new StatisticsResponse()
                .setPostsCount(getPostsCountByUserId(userId))
                .setViewsCount(getViewsCountByUserId(userId))
                .setLikesCount(getLikesCountByUserId(userId))
                .setDislikesCount(getDislikesCountByUserId(userId))
                .setFirstPublication(getFirstPublicationByUserId(userId));
    }

    @Override
    public StatisticsResponse getAllStatistics(Principal principal) throws UnAuthorisedUserException {

        if (!settingsService.getGlobalSettings().isStatisticsIsPublic()){
            if (principal != null && userService.getUserByEmail(principal.getName()).getIsModerator() != 1){
                throw new UnAuthorisedUserException();
            }
        }

        return new StatisticsResponse()
                .setPostsCount(getPostsCount())
                .setViewsCount(getViewsCount())
                .setLikesCount(getLikesCount())
                .setDislikesCount(getDislikesCount())
                .setFirstPublication(getFirstPublication());
    }

    private int getPostsCount(){
        return postRepository.findAll().size();
    }

    private int getPostsCountByUserId(int userId){
        return postRepository.findCountAllPostsByUserId(userId);
    }

    private int getViewsCount(){
        return postRepository.findViewsCount();
    }

    private int getViewsCountByUserId(int userId){
        return postRepository.findViewsCountByUserId(userId);
    }

    private int getLikesCount(){
        return postVoteRepository.findCountLikes();
    }

    private int getLikesCountByUserId(int userId){
        return postVoteRepository.findCountLikesByUser(userId);
    }

    private int getDislikesCount(){
        return postVoteRepository.findCountDislikes();
    }

    private int getDislikesCountByUserId(int userId){
        return postVoteRepository.findCountDislikesByUser(userId);
    }

    private long getFirstPublication(){
        return DateTimeUtil.getTimestamp(postRepository.findFirstPost());
    }

    private long getFirstPublicationByUserId(int userId){
        return DateTimeUtil.getTimestamp(postRepository.findFirstPostByUserId(userId));
    }
}
