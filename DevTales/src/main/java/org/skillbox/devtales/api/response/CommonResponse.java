package org.skillbox.devtales.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse implements ParentResponse {

    private boolean result;
    private Map<String, String> errors;
}
