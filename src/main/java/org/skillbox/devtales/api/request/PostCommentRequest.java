package org.skillbox.devtales.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PostCommentRequest {

    @JsonProperty("parent_id")
    private int parentId;
    @JsonProperty("post_id")
    private int postId;
    private String text;

}
