package org.skillbox.devtales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.skillbox.devtales.config.Constants.ACCESS_DENIED;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserAccessDeniedException extends Exception {

    public UserAccessDeniedException() {
        super(ACCESS_DENIED);
    }
}
