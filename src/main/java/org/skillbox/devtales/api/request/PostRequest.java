package org.skillbox.devtales.api.request;

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
public class PostRequest {

    private Long timestamp;
    private byte active;
    private String title;
    private String text;
    private String[] tags;
}
