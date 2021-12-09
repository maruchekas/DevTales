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
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final int MIL_TO_SEC = 1000;


    private final PostRepository postRepository;

    private final PostVoteRepository postVoteRepository;

    private final ModelMapper modelMapper;

    public PostResponse getAllPosts(Integer offset, Integer limit, String sort, String mode) {

        PostResponse postResponse = new PostResponse();
        Pageable paging = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, sort));
        Page<Post> pageResult = postRepository.findAllActiveAndAcceptedPosts(paging);
        List<PostDto> postDtos = Mapper.convertList(pageResult.getContent(), this::convertPostToDto);

        postResponse.setCount((int) postRepository.count());
        postResponse.setPosts(postDtos);
        return postResponse;

    }

    public PostResponse getActivePosts() {


        return null;
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
