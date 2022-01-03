package org.skillbox.devtales.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.skillbox.devtales.api.response.TagResponse;
import org.skillbox.devtales.dto.TagDto;
import org.skillbox.devtales.model.Tag;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.TagRepository;
import org.skillbox.devtales.repository.TagToPostRepository;
import org.skillbox.devtales.service.TagService;
import org.skillbox.devtales.util.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final PostRepository postRepository;

    private final TagToPostRepository tagToPostRepository;

    private final ModelMapper modelMapper;

    private static double maxWeight;

    public TagResponse getAllTags() {
        TagResponse tagResponse = new TagResponse();
        List<TagDto> tagDtos = Mapper.convertList(tagRepository.findAll(), this::convertTagToDto);
        tagResponse.setTags(tagDtos);
        return tagResponse;
    }

    private TagDto convertTagToDto(Tag tag) {
        TagDto tagDto = modelMapper.map(tag, TagDto.class);
        setWeightForTag(tagDto);
        return tagDto;
    }

    private void setWeightForTag(TagDto tagDto) {
        int countAllPosts = (int) postRepository.count();
        int numOfOccureTagInPosts = tagToPostRepository.findAllTagsById(tagDto.getId()).size();
        double abnormalWeight = (double) numOfOccureTagInPosts / countAllPosts;
        if (maxWeight < abnormalWeight) {
            maxWeight = abnormalWeight;
        }
        double normalWeight = 1 / maxWeight * abnormalWeight;
        tagDto.setWeight(normalWeight);

    }

}
