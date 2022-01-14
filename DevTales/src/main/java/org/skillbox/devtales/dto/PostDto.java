package org.skillbox.devtales.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

    private int id;
    private long timestamp;
    private boolean isActive;
    private UserDto user;
    private String title;
    private String announce;
    private String text;
    private int commentCount;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    List<PostCommentDto> comments;
    Set<String> tags;

    public void setIsActive(int isActive) {
        this.isActive = isActive == 1;
    }
}
