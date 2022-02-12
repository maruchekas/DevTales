package org.skillbox.devtales.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ModeratePostRequest {

    @JsonProperty("post_id")
    private int postId;
    private String decision;
}
