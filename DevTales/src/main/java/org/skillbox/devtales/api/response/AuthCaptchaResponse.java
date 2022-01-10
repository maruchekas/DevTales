package org.skillbox.devtales.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthCaptchaResponse {
    private String secret;
    private String image;
}
