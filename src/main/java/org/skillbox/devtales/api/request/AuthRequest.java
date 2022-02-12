package org.skillbox.devtales.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthRequest {

    @JsonProperty("e_mail")
    private String email;
    private String password;
}
