package org.skillbox.devtales.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModeratePostRequest {

    @JsonProperty("post_id")
    private int postId;
    private String decision;
}
