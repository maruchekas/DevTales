package org.skillbox.devtales.api.response;

import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.dto.PostDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
public class PostResponse {

    private int count;
    private List<PostDto> posts;


}
