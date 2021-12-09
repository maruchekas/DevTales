package org.skillbox.devtales.api.response;

import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.dto.TagDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
public class TagResponse {

    private List<TagDto> tags;

}
