package org.skillbox.devtales.api.response;

import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.dto.TagDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Setter
@Getter
public class TagResponse {

    private Set<TagDto> tags;

}
