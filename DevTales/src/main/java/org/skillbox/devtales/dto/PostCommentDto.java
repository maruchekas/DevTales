package org.skillbox.devtales.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.skillbox.devtales.api.response.UserDataResponse;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCommentDto {
    int id;
    long timestamp;
    String text;
    UserDataResponse user;
}
