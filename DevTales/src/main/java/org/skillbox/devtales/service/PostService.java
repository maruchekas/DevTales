package org.skillbox.devtales.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.skillbox.devtales.api.response.PostResponse;
import org.skillbox.devtales.dto.PostDto;
import org.skillbox.devtales.dto.UserDto;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.PostVoteRepository;
import org.skillbox.devtales.util.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final int MIL_TO_SEC = 1000;

    private final PostRepository postRepository;

    private final PostVoteRepository postVoteRepository;

    private final ModelMapper modelMapper;

    public PostResponse getAllPosts(int offset, int limit, String mode) {

        PostResponse postResponse = new PostResponse();
        Sort sort = Sort.by(Sort.Direction.DESC, "dateTime");

        Pageable pageable = PageRequest.of(offset, limit, sort);
        Page<Post> pagePosts = postRepository.findAllActiveAndAcceptedPosts(pageable);
        List<PostDto> sortedDto =
                getSortedDto(mode, Mapper.convertList(pagePosts.getContent(), this::convertPostToDto));

        postResponse.setCount(postRepository.findAll().size());
        postResponse.setPosts(sortedDto);
        return postResponse;
    }

    private List<PostDto> getSortedDto(String mode, List<PostDto> postDtos) {
        List<PostDto> sortedDto;

        switch (mode) {
            case "best":
                sortedDto = postDtos.stream()
                        .sorted(Comparator.comparing(PostDto::getLikeCount).reversed())
                        .collect(Collectors.toList());
                break;
            case "popular":
                sortedDto = postDtos.stream()
                        .sorted(Comparator.comparing(PostDto::getCommentCount).reversed())
                        .collect(Collectors.toList());
                break;
            case "early":
                sortedDto = postDtos.stream()
                        .sorted(Comparator.comparing(PostDto::getTimestamp))
                        .collect(Collectors.toList());
                break;
            default:
                sortedDto = postDtos.stream()
                        .sorted(Comparator.comparing(PostDto::getTimestamp).reversed())
                        .collect(Collectors.toList());
        }
        return sortedDto;
    }

    public PostDto getOnePostById(int id) {

        return convertPostToDto(postRepository.getById(id));
    }

    private PostDto convertPostToDto(Post post) {
        PostDto postDto = modelMapper.map(post, PostDto.class);
        if (post.getText().length() > 150) {
            String announce = post.getText().substring(0, 150) + "...";
            postDto.setAnnounce(announce);
        } else {
            postDto.setAnnounce(post.getText());
        }

        postDto.setTimestamp(Timestamp.valueOf(post.getDateTime()).getTime() / MIL_TO_SEC);
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
