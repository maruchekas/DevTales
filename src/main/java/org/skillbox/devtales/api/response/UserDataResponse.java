package org.skillbox.devtales.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDataResponse {

    private int id;
    private String name;
    private String photo;
    private String email;
}
