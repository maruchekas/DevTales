package org.skillbox.devtales.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @JsonProperty("e_mail")
    private String email;
    private String name;
    private String password;
    private String captcha;
    @JsonProperty("captcha_secret")
    private String captchaSecret;

}