package org.skillbox.devtales.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.dto.PostDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

    private int count;
    private List<PostDto> posts;


}
