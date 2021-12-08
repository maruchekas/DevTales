package org.skillbox.devtales.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostDto {

    private int id;
    private long timestamp;
    private UserDto user;
    private String title;
    private String announce;
    private String text;
    private int commentCount;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;

}
