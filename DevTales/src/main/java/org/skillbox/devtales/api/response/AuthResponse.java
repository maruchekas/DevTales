package org.skillbox.devtales.api.response;

import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.model.User;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class AuthResponse {

    private boolean result;
    private User user;

}
