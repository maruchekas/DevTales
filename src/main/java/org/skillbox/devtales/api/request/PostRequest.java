package org.skillbox.devtales.api.request;

import lombok.*;
import lombok.experimental.Accessors;
import org.skillbox.devtales.model.Tag;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PostRequest {

    private Long timestamp;
    private byte active;
    private String title;
    private String text;
    private String[] tags;
}
