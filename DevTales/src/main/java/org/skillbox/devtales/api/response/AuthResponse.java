package org.skillbox.devtales.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.model.User;
import org.springframework.stereotype.Component;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private boolean result;
    @JsonProperty("user")
    private AuthUserResponse authUserResponse;

}
