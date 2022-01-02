package org.skillbox.devtales.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CheckResponse {

    private boolean result;

    @JsonProperty("user")
    private AuthUserResponse authUserResponse;
}
