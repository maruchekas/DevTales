package org.skillbox.devtales.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckResponse {

    private boolean result;

    @JsonProperty("user")
    private UserDataResponse userDto;
}
