package org.skillbox.devtales.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCommentDto {
    int id;
    long timestamp;
    String text;
    UserDto user;
}
