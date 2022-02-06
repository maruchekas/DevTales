package org.skillbox.devtales.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.skillbox.devtales.dto.UserDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private boolean result;
    @JsonProperty("user")
    private UserDto userDto;

}
